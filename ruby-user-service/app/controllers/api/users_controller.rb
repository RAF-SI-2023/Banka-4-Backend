class Api::UsersController < ApplicationController
  before_action :set_user, only: %i[update destroy ]

  # GET /api/users
  def index
    @users = User.all

    render json: @users
  end

  # POST /api/users
  def create
    @user = User.new(create_user_params)

    if @user.valid? && @user.save
      render json: @user, status: :ok
    else
      render json: @user.errors, status: :bad_request
    end
  end

  def register
    @user = User.find_by(email: register_user_params[:email])

    if @user.update(register_user_params)
      render json: @user, status: :ok
    else
      render json: @user.errors, status: :bad_request
    end

  end

  # PATCH/PUT /api/users/{id}
  def update
    if @user.update(update_user_params)
      render json: @user, status: :ok
    else
      render json: @user.errors, status: :bad_request
    end
  end

  # DELETE /api/users/{id}
  def destroy
    @user.destroy!
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

  # Only allow the following parameters when updating a user
  def update_user_params
    params.require(:user).permit(:last_name, :address, :phone, :password, :connected_accounts, :active)
  end

  def register_user_params
    params.require(:user).permit(:email, :password, :password_confirmation)
  end

  # Only allow a list of trusted parameters through.
  def user_params
    params.require(:user).permit(:first_name, :last_name, :jmbg, :birth_date, :gender, :email, :password, :password_confirmation, :phone, :address, :connected_accounts, :active)
  end
end