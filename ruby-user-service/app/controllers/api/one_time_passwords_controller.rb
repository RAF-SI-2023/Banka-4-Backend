class Api::OneTimePasswordsController < ApplicationController
  before_action :set_one_time_password, only: %i[ show update destroy ]

  # GET /one_time_passwords
  def index
    @one_time_passwords = OneTimePassword.all

    render json: @one_time_passwords
  end

  # GET /one_time_passwords/1
  def show
    render json: @one_time_password
  end

  # POST /one_time_passwords
  def create
    @one_time_password = OneTimePassword.new(one_time_password_params)

    if @one_time_password.save
      render json: @one_time_password, status: :created, location: @one_time_password
    else
      render json: @one_time_password.errors, status: :unprocessable_entity
    end
  end

  # PATCH/PUT /one_time_passwords/1
  def update
    if @one_time_password.update(one_time_password_params)
      render json: @one_time_password
    else
      render json: @one_time_password.errors, status: :unprocessable_entity
    end
  end

  # DELETE /one_time_passwords/1
  def destroy
    @one_time_password.destroy!
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_one_time_password
      @one_time_password = OneTimePassword.find(params[:id])
    end

    # Only allow a list of trusted parameters through.
    def one_time_password_params
      params.require(:one_time_password).permit(:email, :password, :password_confirmation, :expiration)
    end
end
