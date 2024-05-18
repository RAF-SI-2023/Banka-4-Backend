class Api::VerificationCodesController < ApplicationController
  # before_action :wrap_params
  before_action :authenticate_user

  # POST /api/verification_codes/register
  def create_register_code
    @verification_code = VerificationCode.find_or_initialize_by(email: params[:email], reset: false)
    @verification_code.code = SecureRandom.uuid
    @verification_code.expiration = (Time.now + 5.minutes).to_i * 1000

    if @verification_code.valid? && @verification_code.save
      render json: @verification_code, status: :ok
    else
      render_bad_request
    end
  end

  # POST /api/verification_codes/reset
  def create_reset_code
    @verification_code = VerificationCode.find_or_initialize_by(email: params[:email], reset: true)
    @verification_code.code = SecureRandom.uuid
    @verification_code.expiration = (Time.now + 5.minutes).to_i * 1000

    if @verification_code.valid? && @verification_code.save
      render json: @verification_code, status: :ok
    else
      render_bad_request
    end
  end

  private

  # Only allow select params when creating a verification code
  def create_verification_code_param
    params.require(:verification_code).permit(:email)
  end

  def render_bad_request
    render json: @worker.errors, status: :bad_request
  end

  def set_verification_code
    @verification_code = VerificationCode.find(params[:id])
  end

  def render_unauthorized
    render json: { error: 'Unauthorized' }, status: :unauthorized
  end

  def authenticate_user
    authorization_header = request.headers['Authorization']
    render_unauthorized if authorization_header && authorization_header.start_with?('Bearer ')
  end

  # def wrap_params
  # return if params[:verification_code]

  # params[:verification_code] = params.permit!.to_h
  # end
end
