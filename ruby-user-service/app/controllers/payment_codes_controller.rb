class PaymentCodesController < ApplicationController
  before_action :set_payment_code, only: %i[ show update destroy ]

  # GET /payment_codes
  def index
    @payment_codes = PaymentCode.all

    render json: @payment_codes
  end

  # GET /payment_codes/1
  def show
    render json: @payment_code
  end

  # POST /payment_codes
  def create
    @payment_code = PaymentCode.new(payment_code_params)

    if @payment_code.save
      render json: @payment_code, status: :created, location: @payment_code
    else
      render json: @payment_code.errors, status: :unprocessable_entity
    end
  end

  # PATCH/PUT /payment_codes/1
  def update
    if @payment_code.update(payment_code_params)
      render json: @payment_code
    else
      render json: @payment_code.errors, status: :unprocessable_entity
    end
  end

  # DELETE /payment_codes/1
  def destroy
    @payment_code.destroy!
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_payment_code
      @payment_code = PaymentCode.find(params[:id])
    end

    # Only allow a list of trusted parameters through.
    def payment_code_params
      params.require(:payment_code).permit(:form_and_basis, :payment_description)
    end
end
