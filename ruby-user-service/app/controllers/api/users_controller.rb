class Api::UsersController < ApplicationController
  # before_action :set_user, only: %i[ show update destroy ]

  # POST /api/users
  def create
    @user = User.new(create_user_params)

    if @user.valid? && @user.save
      render json: @user, status: :ok, location: @user
    else
      render json: @user.errors, status: :bad_request
    end
  end

  private

  # Use callbacks to share common setup or constraints between actions.
  def set_user
    @user = User.find(params[:id])
  end

  # Only allow the following parameters when creating a user
  def create_user_params
    params.require(:user).permit(:first_name, :last_name, :jmbg, :birth_date, :gender, :email, :phone, :address, :connected_accounts, :active)
  end

  # Only allow a list of trusted parameters through.
  def user_params
    params.require(:user).permit(:first_name, :last_name, :jmbg, :birth_date, :gender, :email, :password, :password_confirmation, :phone, :address, :connected_accounts, :active)
  end
end