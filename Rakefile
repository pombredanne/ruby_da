require "bundler/gem_tasks"
require "rake/testtask"

Rake::TestTask.new(:test) do |t|
  t.libs << "test"
end

task :default => :test
task :build => :compile

if RUBY_PLATFORM =~ /java/
  BUILD_FILE = File.join(File.dirname(__FILE__), 'java_da', 'build.xml')
  JAR_SRC_FILE = File.join(File.dirname(__FILE__), 'java_da', 'target', 'java-da.jar')
  JAR_DEST_FILE = File.join(File.dirname(__FILE__), 'lib', 'java-da.jar')


  task :pre_compile do
    `ant -f #{BUILD_FILE} jar`
    rm_f(JAR_DEST_FILE)
    mv(JAR_SRC_FILE, JAR_DEST_FILE)
  end
  task :compile => :pre_compile
  require "rake/javaextensiontask"
  Rake::JavaExtensionTask.new('ruby_da') do |ext|
    ext.ext_dir = "ext/ruby_da"
    ext.classpath = [JAR_DEST_FILE]
    ext.source_version = '1.8'
    ext.target_version = '1.8'
  end
  CLEAN << File.join(File.dirname(__FILE__), 'java_da', 'target')
  CLOBBER << JAR_DEST_FILE
else
  require "rake/extensiontask"
  Rake::ExtensionTask.new("ruby_da") do |ext|
    ext.lib_dir = "lib/ruby_da"
  end
end
