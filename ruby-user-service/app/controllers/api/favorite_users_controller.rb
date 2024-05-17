class Api::FavoriteUsersController < ApplicationController
  before_action :set_favorite_user, only: %i[ show update destroy ]

  # GET /favorite_users
  def index
    @favorite_users = FavoriteUser.all

    render json: @favorite_users
  end

  # POST /favorite_users
  def create
    @favorite_user = FavoriteUser.new(favorite_user_params)

    if @favorite_user.save
      render json: @favorite_user, status: :created, location: @favorite_user
    else
      render json: @favorite_user.errors, status: :unprocessable_entity
    end
  end

  # PATCH/PUT /favorite_users/1
  def update
    if @favorite_user.update(favorite_user_params)
      render json: @favorite_user
    else
      render json: @favorite_user.errors, status: :unprocessable_entity
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
    params.require(:favorite_user).permit(:userId, :sender_account_number, :sender_name, :sender_account_number, :number, :payment_code)
  end

  def wrap_params
    return if params[:favorite_user]

    params[:favorite_user] = params.permit!.to_h
  end
end
