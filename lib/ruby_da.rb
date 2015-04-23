require "ruby_da/version"

module RubyDa
  if RUBY_PLATFORM =~ /java/
    require File.join(File.dirname(File.expand_path(__FILE__)), "java-da.jar")
    require File.join(File.dirname(File.expand_path(__FILE__)), "ruby_da.jar")
    require 'jruby'
    Java::RubyDaService.new.basicLoad(JRuby.runtime)
  else
    require "ruby_da/ruby_da"
  end
end
