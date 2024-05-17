class Api::AuthController < ApplicationController
  # POST /api/authorization/login
  def login
    @token = AuthService.login(login_params[:email], login_params[:password])

    if @token
      render json: @token, status: :ok
    else
      render json: { error: "Invalid email or password" }, status: :bad_request
    end
  end

  private

  def login_params
    params.require(:auth).permit(:email, :password)
  end
end
