require 'rufus-scheduler'

scheduler = Rufus::Scheduler.new

scheduler.every '1m' do
  current_time = Time.now.to_i * 1000

  VerificationCode.where("expiration < ?", current_time).destroy_all
  OneTimePassword.where("expiration < ?", current_time).destroy_all
end