require "test_helper"

class OneTimePasswordTest < ActiveSupport::TestCase
  def setup
    @otp = one_time_passwords(:one)
  end

  test "should be valid with valid attributes" do
    assert @otp.valid?
  end

  test "email should be present" do
    @otp.email = nil
    assert_not @otp.valid?
    assert_includes @otp.errors[:email], "email can't be blank"
  end

  test "email should be valid format" do
    @otp.email = 'invalid-email'
    assert_not @otp.valid?
    assert_includes @otp.errors[:email], "is invalid"
  end

  test "one_time_password should be present" do
    @otp.one_time_password = nil
    assert_not @otp.valid?
    assert_includes @otp.errors[:one_time_password], "one time password can't be blank"
  end

  test "one_time_password should be 6 characters long" do
    @otp.one_time_password = '12345'
    assert_not @otp.valid?
    assert_includes @otp.errors[:one_time_password], "one time password has to be 6 characters long"

    @otp.one_time_password = '1234567'
    assert_not @otp.valid?
    assert_includes @otp.errors[:one_time_password], "one time password has to be 6 characters long"
  end

  test "expiration should be present" do
    @otp.expiration = nil
    assert_not @otp.valid?
    assert_includes @otp.errors[:expiration], "expiration can't be blank"
  end

  test "expiration should be in the future" do
    @otp.expiration = Time.now.to_i * 1000 - 1000
    assert_not @otp.valid?
    assert_includes @otp.errors[:expiration], "expiration must be in the future"
  end
end