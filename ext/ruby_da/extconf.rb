require "mkmf"
RbConfig::CONFIG["CPP"] = "g++ -E"
$LDFLAGS += " -lstdc++"

dir_config('da')
find_header('double_array.hpp', File.dirname(__FILE__) + "/../../libda/include")
if have_header('double_array.hpp')
   create_makefile("ruby_da/ruby_da")
end
