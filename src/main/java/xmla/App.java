/*
 * Copyright (C) 2009-2011 Mathias Doenitz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xmla;

import org.parboiled.BaseParser;
import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.parserunners.RecoveringParseRunner;

@SuppressWarnings({"InfiniteRecursion"})
@BuildParseTree
public class App extends BaseParser<Object> {
	public Rule Go() { return Atom(); }
	
	public Rule Atom() {
		return NoneOf("aaa");
	}

	public Rule LetterOrDigit() {
		return FirstOf(CharRange('a', 'z'), CharRange('A', 'Z'), CharRange('0', '9'), '_', '$', UnicodeEscape());
	}

	public Rule UnicodeEscape() {
        return Sequence(OneOrMore('u'), HexDigit(), HexDigit(), HexDigit(), HexDigit());
    }
	
	public Rule HexDigit() {
        return FirstOf(CharRange('a', 'f'), CharRange('A', 'F'), CharRange('0', '9'));
    }

	public static void main(String[] args) {
		App parser = Parboiled.createParser(App.class);
		var r = new RecoveringParseRunner(parser.Go()).run("bbb").hasErrors();
		System.out.print(r);
//        Rule rule = parser.Clause();
	}
}
