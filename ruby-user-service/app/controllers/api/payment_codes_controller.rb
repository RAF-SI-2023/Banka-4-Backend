class Api::PaymentCodesController < ApplicationController
  before_action :set_payment_code, only: %i[ show update destroy ]

  # GET /payment_codes
  def index
    @payment_codes = PaymentCode.all

    render json: @payment_codes
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
