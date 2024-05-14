Rails.application.routes.draw do
  namespace :api, path: "/api" do
    resources :favorite_users
    resources :verification_codes
    resources :payment_codes
    resources :one_time_passwords
    resources :workers
    resources :users, only: [:create, :index, :update, :destroy]

    post "users/register" => "users#register"

    namespace :registrations, path: "/registrations" do
      devise_for :workers, controllers: { registrations: 'api/registrations' }
      devise_for :users, controllers: { registrations: 'api/registrations' }
    end
  end
  # Define your application routes per the DSL in https://guides.rubyonrails.org/routing.html

  # Reveal health status on /up that returns 200 if the app boots with no exceptions, otherwise 500.
  # Can be used by load balancers and uptime monitors to verify that the app is live.
  get "up" => "rails/health#show", as: :rails_health_check

  # Defines the root path route ("/")
  # root "posts#index"
end
