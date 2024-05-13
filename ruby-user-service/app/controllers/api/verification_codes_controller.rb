class Api::VerificationCodesController < ApplicationController
  before_action :set_verification_code, only: %i[ show update destroy ]

  # GET /verification_codes
  def index
    @verification_codes = VerificationCode.all

    render json: @verification_codes
  end

  # GET /verification_codes/1
  def show
    render json: @verification_code
  end

  # POST /verification_codes
  def create
    @verification_code = VerificationCode.new(verification_code_params)

    if @verification_code.save
      render json: @verification_code, status: :created, location: @verification_code
    else
      render json: @verification_code.errors, status: :unprocessable_entity
    end
  end

  # PATCH/PUT /verification_codes/1
  def update
    if @verification_code.update(verification_code_params)
      render json: @verification_code
    else
      render json: @verification_code.errors, status: :unprocessable_entity
    end
  end

  # DELETE /verification_codes/1
  def destroy
    @verification_code.destroy!
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_verification_code
      @verification_code = VerificationCode.find(params[:id])
    end

    # Only allow a list of trusted parameters through.
    def verification_code_params
      params.require(:verification_code).permit(:email, :code, :expiration, :reset)
    end
end
