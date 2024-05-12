require "test_helper"

class PaymentCodesControllerTest < ActionDispatch::IntegrationTest
  setup do
    @payment_code = payment_codes(:one)
  end

  test "should get index" do
    get payment_codes_url, as: :json
    assert_response :success
  end

  test "should create payment_code" do
    assert_difference("PaymentCode.count") do
      post payment_codes_url, params: { payment_code: { form_and_basis: @payment_code.form_and_basis, payment_description: @payment_code.payment_description } }, as: :json
    end

    assert_response :created
  end

  test "should show payment_code" do
    get payment_code_url(@payment_code), as: :json
    assert_response :success
  end

  test "should update payment_code" do
    patch payment_code_url(@payment_code), params: { payment_code: { form_and_basis: @payment_code.form_and_basis, payment_description: @payment_code.payment_description } }, as: :json
    assert_response :success
  end

  test "should destroy payment_code" do
    assert_difference("PaymentCode.count", -1) do
      delete payment_code_url(@payment_code), as: :json
    end

    assert_response :no_content
  end
end
