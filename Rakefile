require "bundler/gem_tasks"
require "rake/testtask"

Rake::TestTask.new(:test) do |t|
  t.libs << "test"
end

task :default => :test
require "rake/extensiontask"

task :build => :compile

Rake::ExtensionTask.new("ruby_da") do |ext|
  ext.lib_dir = "lib/ruby_da"
  ext.config_options = "--with-da-include=#{File.dirname(__FILE__)}/libda/include"
end