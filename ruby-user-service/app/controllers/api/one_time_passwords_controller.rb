class Api::OneTimePasswordsController < ApplicationController
  before_action :authenticate_user

  # GET /one_time_passwords
  def index
    @one_time_passwords = OneTimePassword.all

    render json: @one_time_passwords
  end

  # POST /one_time_passwords
  def create
    @one_time_password = OneTimePassword.find_or_initialize_by(email: one_time_password_params[:email])
    @one_time_password.one_time_password = rand.to_s[2..7]

    if @one_time_password.save
      render json: @one_time_password, status: :ok
    else
      render_bad_request
    end
  end

  def check
    render_bad_request(error: "One time password not requested or doesn't match") unless (@one_time_password = OneTimePassword.find_by(check_one_time_password_params))

    if @one_time_password
      render json: @one_time_password, status: :ok
    else
      render_bad_request
    end
  end

  private

  # Use callbacks to share common setup or constraints between actions.
  def set_one_time_password
    @one_time_password = OneTimePassword.find(params[:id])
  end

  # Only allow a list of trusted parameters through.
  def one_time_password_params
    params.require(:one_time_password).permit(:email, :one_time_password, :expiration)
  end

  def check_one_time_password_params
    params.require(:one_time_password).permit(:email, :one_time_password)
  end

  def render_unauthorized
    render json: { error: 'Unauthorized' }, status: :unauthorized
  end

  def render_bad_request(error: nil)
    error_message = error || @one_time_password.errors
    render json: { error: error_message }, status: :bad_request
  end

  def authenticate_user
    authorization_header = request.headers['Authorization']
    return render_unauthorized unless authorization_header && authorization_header.start_with?('Bearer ')

    token = authorization_header.split(' ').last
    return render_unauthorized unless token

    @current_user = AuthService.authenticate_user(token)
    render_unauthorized unless @current_user
  end
end
