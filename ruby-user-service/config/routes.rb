Rails.application.routes.draw do
  namespace :api, path: "/api" do
    resources :favorite_users
    resources :verification_codes
    resources :payment_codes
    resources :one_time_passwords
    resources :workers
    resources :users

    post "auth/login", to: "auth#login"

    post "users/register", to: "users#register"

    post "verification_codes/register", to: "verification_codes#create_register_code"
    post "verification_codes/reset", to: "verification_codes#create_reset_code"
  end
  # Define your application routes per the DSL in https://guides.rubyonrails.org/routing.html

  # Reveal health status on /up that returns 200 if the app boots with no exceptions, otherwise 500.
  # Can be used by load balancers and uptime monitors to verify that the app is live.
  # get "up" => "rails/health#show", as: :rails_health_check

  # Defines the root path route ("/")
  # root "posts#index"
end
