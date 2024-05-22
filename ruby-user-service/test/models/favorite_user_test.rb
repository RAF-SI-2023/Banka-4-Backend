require "test_helper"

class FavoriteUserTest < ActiveSupport::TestCase
  def setup
    @favorite_user = favorite_users(:one)
  end

  test "should be valid with valid attributes" do
    assert @favorite_user.valid?
  end

  test "user_id should be present" do
    @favorite_user.user_id = nil
    assert_not @favorite_user.valid?
    assert_includes @favorite_user.errors[:user_id], "user id can't be blank"
  end

  test "sender_name should be present" do
    @favorite_user.sender_name = nil
    assert_not @favorite_user.valid?
    assert_includes @favorite_user.errors[:sender_name], "sender name can't be blank"
  end

  test "sender_account_number should be present, 18 digits long, and valid format" do
    @favorite_user.sender_account_number = nil
    assert_not @favorite_user.valid?
    assert_includes @favorite_user.errors[:sender_account_number], "sender id can't be blank"

    @favorite_user.sender_account_number = '12345678901234567' # 17 digits
    assert_not @favorite_user.valid?
    assert_includes @favorite_user.errors[:sender_account_number], "sender account number must be 18 digits"

    @favorite_user.sender_account_number = 'abc123456789012345' # non-numeric
    assert_not @favorite_user.valid?
    assert_includes @favorite_user.errors[:sender_account_number], "sender account number must be 18 digits"

    @favorite_user.sender_account_number = '123456789012345678' # 18 digits and valid format
    assert @favorite_user.valid?
  end

  test "receiver_account_number should be present, 18 digits long, and valid format" do
    @favorite_user.receiver_account_number = nil
    assert_not @favorite_user.valid?
    assert_includes @favorite_user.errors[:receiver_account_number], "receiver id can't be blank"

    @favorite_user.receiver_account_number = '12345678901234567' # 17 digits
    assert_not @favorite_user.valid?
    assert_includes @favorite_user.errors[:receiver_account_number], "receiver account number must be 18 digits"

    @favorite_user.receiver_account_number = 'abc123456789012345' # non-numeric
    assert_not @favorite_user.valid?
    assert_includes @favorite_user.errors[:receiver_account_number], "receiver account number must be 18 digits"

    @favorite_user.receiver_account_number = '123456789012345678' # 18 digits and valid format
    assert @favorite_user.valid?
  end

  test "number should be present" do
    @favorite_user.number = nil
    assert_not @favorite_user.valid?
    assert_includes @favorite_user.errors[:number], "number can't be blank"
  end

  test "payment_code should be present" do
    @favorite_user.payment_code = nil
    assert_not @favorite_user.valid?
    assert_includes @favorite_user.errors[:payment_code], "payment code can't be blank"
  end
end