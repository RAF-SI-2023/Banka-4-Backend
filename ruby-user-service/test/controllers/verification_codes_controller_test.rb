require "test_helper"

class VerificationCodesControllerTest < ActionDispatch::IntegrationTest
  setup do
    @verification_code = verification_codes(:one)
  end

  test "should get index" do
    get verification_codes_url, as: :json
    assert_response :success
  end

  test "should create verification_code" do
    assert_difference("VerificationCode.count") do
      post verification_codes_url, params: { verification_code: {  } }, as: :json
    end

    assert_response :created
  end

  test "should show verification_code" do
    get verification_code_url(@verification_code), as: :json
    assert_response :success
  end

  test "should update verification_code" do
    patch verification_code_url(@verification_code), params: { verification_code: {  } }, as: :json
    assert_response :success
  end

  test "should destroy verification_code" do
    assert_difference("VerificationCode.count", -1) do
      delete verification_code_url(@verification_code), as: :json
    end

    assert_response :no_content
  end
end
