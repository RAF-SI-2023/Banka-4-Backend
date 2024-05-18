class Api::AuthController < ApplicationController
  # POST /api/authorization/login
  def login
    @token = AuthService.login(login_params[:email], login_params[:password])

    if @token
      render json: @token, status: :ok
    else
      render_bad_request(error: "Invalid username or password")
    end
  end

  # POST /api/auth/forgot_password
  def forgot_password
    render_bad_request(error: "User not found") unless (@user = User.find_by_email(forgot_password_params[:email]))

    return render_bad_request(error: "Verification code not given") unless params.key?(:verification_code)
    return render_bad_request(error: "Verification code not requested or doesn't match") unless (@verification_code = VerificationCode.find_by(email: forgot_password_params[:email], code: forgot_password_params[:verification_code], reset: true))

    if @user.update(forgot_password_params.except(:verification_code))
      @verification_code.destroy!
      render json: @user, status: :ok
    else
      render_bad_request
    end
  end

  # POST /api/auth/reset_password
  def reset_password
    return render_bad_request(error: "User not found") unless (@user = User.find_by_email(reset_password_params[:email]))
    render_bad_request(error: "Password doesn't match") unless @user.password == reset_password_params[:old_password]

    if @user.update(reset_password_params.except(:verification_code, :old_password))
      @verification_code.destroy!
      render json: @user, status: :ok
    else
      render_bad_request
    end
  end

  private

  def login_params
    params.require(:auth).permit(:email, :password)
  end

  def forgot_password_params
    params.require(:auth).permit(:email, :password, :verification_code)
  end

  def reset_password_params
    params.require(:auth).permit(:email, :old_password, :password)
  end

  def render_unauthorized
    render json: { error: 'Unauthorized' }, status: :unauthorized
  end

  def render_bad_request(error: nil)
    error_message = error || @user.errors
    render json: { error: error_message }, status: :bad_request
  end
end
