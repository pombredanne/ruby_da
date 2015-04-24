# coding: utf-8
lib = File.expand_path('../lib', __FILE__)
$LOAD_PATH.unshift(lib) unless $LOAD_PATH.include?(lib)
require 'ruby_da/version'

Gem::Specification.new do |spec|
  spec.name          = "ruby_da"
  spec.version       = RubyDa::VERSION
  spec.authors       = ["Masahiko Higashiyama"] 
  spec.email         = ["masahiko.higashiyama@gmail.com"]

  spec.summary       = %q{Double Array Implementation}
  spec.description   = %q{Double Array Implementation}
  spec.homepage      = "https://github.com/shnya/ruby_da"
  spec.license       = "MIT"

  spec.files         = `git ls-files -z`.split("\x0").reject { |f| f.match(%r{^(test|spec|features)/}) }
  spec.bindir        = "exe"
  spec.executables   = spec.files.grep(%r{^exe/}) { |f| File.basename(f) }
  spec.require_paths = ["lib"]
  if RUBY_PLATFORM =~ /java/
    spec.platform = "java"
    spec.files << "lib/java-da.jar"
    spec.files << "lib/ruby_da.jar"
    spec.files << `find ./libda/include -print0 -name "*.hpp"`.split("\x0")
  else
    spec.extensions    = ["ext/ruby_da/extconf.rb"]
    spec.files.concat(`find ./libda -print0 -name "*.hpp"`.split("\x0").reject{ |f| f.match(%r{(.git|test)}) })
  end

  spec.add_development_dependency "bundler", "~> 1.9"
  spec.add_development_dependency "rake", "~> 10.0"
  spec.add_development_dependency "rake-compiler"
  spec.add_development_dependency "minitest"
end
