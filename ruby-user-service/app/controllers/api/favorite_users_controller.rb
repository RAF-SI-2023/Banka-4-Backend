class Api::FavoriteUsersController < ApplicationController
  before_action :set_favorite_user, only: %i[ update destroy ]
  before_action :authenticate_user

  # GET /favorite_users
  def index
    @favorite_users = FavoriteUser.where(user_id: @current_user.id)

    render json: @favorite_users
  end

  # POST /favorite_users
  def create
    @favorite_user = FavoriteUser.new(favorite_user_params)
    @favorite_user.user_id = @current_user.id

    if @favorite_user.save
      render json: @favorite_user, status: :ok
    else
      render_bad_request
    end
  end

  # PATCH/PUT /favorite_users/1
  def update
    if @favorite_user.update(favorite_user_params)
      render json: @favorite_user
    else
      render_bad_request
    end
  end

  # DELETE /favorite_users/1
  def destroy
    @favorite_user.destroy!
  end

  private

  # Use callbacks to share common setup or constraints between actions.
  def set_favorite_user
    @favorite_user = FavoriteUser.find(params[:id])
  end

  # Only allow a list of trusted parameters through.
  def favorite_user_params
    params.require(:favorite_user).permit(:sender_account_number, :sender_name, :sender_account_number, :number, :payment_code)
  end

  def render_unauthorized
    render json: { error: 'Unauthorized' }, status: :unauthorized
  end

  def render_bad_request(error: nil)
    error_message = error || @favorite_user.errors
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
