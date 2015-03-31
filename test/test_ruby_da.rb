# -*- coding: utf-8 -*-
require 'minitest_helper'

class TestRubyDa < Minitest::Test
  def test_that_it_has_a_version_number
    refute_nil ::RubyDa::VERSION
  end

  def test_insert
    trie = RubyDa::Trie.new
    trie.build(["日本", "日本の夜明け", "日本の夜明け前", "Yahoo"])
    trie.extract_all_matched("Yahoo!日本の夜明けは明るい").each{|word|
      puts word.encoding
    }
  end
end
