import org.jcodings.Encoding;
import org.jruby.*;
import org.jruby.anno.JRubyClass;
import org.jruby.anno.JRubyMethod;
import org.jruby.runtime.ObjectAllocator;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.runtime.load.BasicLibraryService;
import jp.scaleout.DoubleArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class RubyDaService implements BasicLibraryService {
    private Ruby runtime;

    @Override
    public boolean basicLoad(Ruby runtime) throws IOException {
        this.runtime = runtime;
        RubyModule rbModule = runtime.defineModule("RubyDa");

        RubyClass rbClass = rbModule.defineClassUnder("Trie", this.runtime.getObject(), new ObjectAllocator() {
            public IRubyObject allocate(Ruby runtime, RubyClass rubyClass) {
                return new Trie(runtime, rubyClass);
            }
        });
        rbClass.defineAnnotatedMethods(Trie.class);
        return true;
    }

    @JRubyClass(name="Trie")
    public class Trie extends RubyObject {
        private DoubleArray da = null;

        public Trie(Ruby runtime, RubyClass metaClass) {
            super(runtime, metaClass);
        }

        @JRubyMethod
        public IRubyObject initialize(ThreadContext context){
            this.da = new DoubleArray();
            return this;
        }


        private IRubyObject convToArray(List<String> from){
            RubyArray res = this.getRuntime().newArray(from.size());
            for(String entry : from){
                RubyString resStr = this.getRuntime().newString(entry);
                resStr.associateEncoding(Encoding.load("UTF8"));
                res.add(resStr);
            }
            return res;
        }

        private IRubyObject convToBoolean(boolean arg){
            return arg ? this.getRuntime().getTrue() : this.getRuntime().getFalse();
        }

        @JRubyMethod(name = "enumerate")
        public IRubyObject enumerate(ThreadContext context, IRubyObject arg){
            String str = ((RubyString)arg).decodeString();
            List<String> resStrs = new ArrayList<String>();
            List<Integer> resIds = new ArrayList<Integer>();
            da.enumerate(str, resStrs, resIds);
            RubyArray res = this.getRuntime().newArray(resStrs.size());
            for(int i = 0; i < resStrs.size(); i++){
                RubyArray elem = this.getRuntime().newArray(2);
                RubyString resStr = this.getRuntime().newString(resStrs.get(i));
                resStr.associateEncoding(Encoding.load("UTF8"));
                elem.add(resStr);
                elem.add(RubyNumeric.int2fix(this.getRuntime(), resIds.get(i)));
                res.add(elem);
            }
            return res; 
        }

        @JRubyMethod(name = {"common_prefix_search", "commonPrefixSearch"})
        public IRubyObject commonPrefixSearch(ThreadContext context, IRubyObject str){
            return convToArray(da.commonPrefixSearch(str.asJavaString()));
        }

        @JRubyMethod(name = "contains")
        public IRubyObject contains(ThreadContext context, IRubyObject str){
            return convToBoolean(da.contains(((RubyString) str).decodeString()));
        }

        @JRubyMethod(name = {"extract_all_matched", "extractAllMatched"})
        public IRubyObject extractAllMatched(ThreadContext context, IRubyObject str){
            return convToArray(da.extractAllMatched(str.asJavaString()));
        }

        @JRubyMethod(name = "insert", required=1, optional=1)
        public IRubyObject insert(ThreadContext context, IRubyObject[] args){
            String str = ((RubyString)args[0]).decodeString();
            if(args.length > 1){
                int id = RubyInteger.num2int(args[1]);
                return convToBoolean(da.insert(str, id));
            }else{
                return convToBoolean(da.insert(str));
            }
        }

        @JRubyMethod(name = "erase")
        public IRubyObject erase(ThreadContext context, IRubyObject arg){
            String str = ((RubyString)arg).decodeString();
            return convToBoolean(da.erase(str));
        }

        @JRubyMethod(name = "build")
        public IRubyObject build(ThreadContext context, IRubyObject arg) {
            RubyArray array = (RubyArray) arg;
            List<String> words = new ArrayList<String>();
            for (Object elem : array) {
                if(elem instanceof RubyString){
                    words.add(((RubyString) elem).decodeString());
                }else{
                    words.add((String)elem);
                }
            }
            return convToBoolean(da.build(words));
        }

        @JRubyMethod(name = "save")
        public IRubyObject save(ThreadContext context, IRubyObject arg) {
            String filename = ((RubyString)arg).decodeString();
            try{
                da.save(filename);
            }catch(IOException e){
                return this.getRuntime().getFalse();
            }
            return this.getRuntime().getTrue();
        }

        @JRubyMethod(name = "load")
        public IRubyObject load(ThreadContext context, IRubyObject arg) {
            String filename = ((RubyString)arg).decodeString();
            try{
                da.load(filename);
            }catch(IOException e){
                return this.getRuntime().getFalse();
            }
            return this.getRuntime().getTrue();
        }
    }
}
