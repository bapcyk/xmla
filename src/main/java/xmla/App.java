package xmla;


public class App {

	public static int main(String[] args) {
		if (2 != args.length) {
			System.out.print("Syntax error: xmla|xml FILE");
			return 1;
		} else {
			if ("xmla" == args[1]) {
				System.out.print("AS XMLA");
			} else if ("xml" == args[1]) {
				System.out.print("AS XML");
			}
		}
		return 0;
	}

}


class ArithmeticCalculator extends XmlaAnalyzer {

}