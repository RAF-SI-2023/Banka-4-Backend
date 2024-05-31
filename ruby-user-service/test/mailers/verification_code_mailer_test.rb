require "test_helper"

class VerificationCodeMailerTest < ActionMailer::TestCase
  def setup
    @verification_code = verification_codes(:one)
  end

  test "should send reset code email" do
    email = VerificationCodeMailer.with(verification_code: @verification_code).send_verification_code_email

    assert_not_nil email
    assert_equal [@verification_code.email], email.to
    assert_equal "Reset Code", email.subject
  end

  test "should send verification code email" do
    @verification_code.update(reset: false)

    email = VerificationCodeMailer.with(verification_code: @verification_code).send_verification_code_email

    assert_not_nil email
    assert_equal [@verification_code.email], email.to
    assert_equal "Verification Code", email.subject
  end
end