# xmla
## XML Alternative

### Proof-of-concept

Test: `xmla.bat --to XML test.xmla` and `xmla.bat --to XMLA test.xml`. Option `-h` is for full help.

<table>
  <tr>
    <td>
      
```
p1 <
	a(href=www name="some name" xxx) <
		br. br. p2<>>
	a<
	<<aaaaa>> <<bbbbb>>
	br.
	br.
        .(lt) .(quot) .(gt) .(amp)
        b2 <
            <<b-content>>
        >
	<<cccc>>
	>
	c <- <<r:   alalalala
		bbbbb .(lt) .(GT)
		cccc>>
>
```
              
</td>
<td>
 
```
<p1>
  <a href="www" name="some name" xxx="">
    <br/> <br/>
    <p2></p2>
  </a>
  <a>
    aaaaa bbbbb
    <br/>
    <br/>
    &lt; &quot; &gt; &amp;
    <b2>
      b-content
    </b2>
    cccc
  </a>
  <c>
    alalalala
    bbbbb &lt; &gt;
    cccc
  </c>
</p1>
```
  
</td>
</tr>
</table>
      
It's good for AST-like scripting like Apache Jelly - https://en.wikipedia.org/wiki/Apache_Jelly:
      
```
for(var=i from=0 to=10) <
      print <- <<Hello world>>
>
```
