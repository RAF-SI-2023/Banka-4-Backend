namespace :db do
  desc "Clean up expired codes"
  task :cleanup => :environment do
    current_time = Time.now.to_i * 1000

    VerificationCode.where("expiration < ?", current_time).destroy_all
    OneTimePassword.where("expiration < ?", current_time).destroy_all
  end
end