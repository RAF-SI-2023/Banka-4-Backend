require "test_helper"

class FavoriteUsersControllerTest < ActionDispatch::IntegrationTest
  setup do
    @favorite_user = favorite_users(:one)
  end

  test "should get index" do
    get favorite_users_url, as: :json
    assert_response :success
  end

  test "should create favorite_user" do
    assert_difference("FavoriteUser.count") do
      post favorite_users_url, params: { favorite_user: {  } }, as: :json
    end

    assert_response :created
  end

  test "should show favorite_user" do
    get favorite_user_url(@favorite_user), as: :json
    assert_response :success
  end

  test "should update favorite_user" do
    patch favorite_user_url(@favorite_user), params: { favorite_user: {  } }, as: :json
    assert_response :success
  end

  test "should destroy favorite_user" do
    assert_difference("FavoriteUser.count", -1) do
      delete favorite_user_url(@favorite_user), as: :json
    end

    assert_response :no_content
  end
end
