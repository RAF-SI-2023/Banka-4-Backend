class VerificationCodeMailer < ApplicationMailer
  default from: ENV["MAILER_USERNAME"]
  layout 'mailer'

  def send_verification_code_email
    @verification_code = params[:verification_code]
    subject = @verification_code.reset ? "Reset Code" : "Verification Code"

    mail(to: @verification_code.email, subject: subject) do |format|
      format.html { render 'verification_code_mailer/email' }
      format.text { render 'verification_code_mailer/email' }
    end
  end
end
