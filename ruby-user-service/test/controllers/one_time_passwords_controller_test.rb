require "test_helper"

class OneTimePasswordsControllerTest < ActionDispatch::IntegrationTest
  setup do
    @one_time_password = one_time_passwords(:one)
  end

  test "should get index" do
    get one_time_passwords_url, as: :json
    assert_response :success
  end

  test "should create one_time_password" do
    assert_difference("OneTimePassword.count") do
      post one_time_passwords_url, params: { one_time_password: {  } }, as: :json
    end

    assert_response :created
  end

  test "should show one_time_password" do
    get one_time_password_url(@one_time_password), as: :json
    assert_response :success
  end

  test "should update one_time_password" do
    patch one_time_password_url(@one_time_password), params: { one_time_password: {  } }, as: :json
    assert_response :success
  end

  test "should destroy one_time_password" do
    assert_difference("OneTimePassword.count", -1) do
      delete one_time_password_url(@one_time_password), as: :json
    end

    assert_response :no_content
  end
end
