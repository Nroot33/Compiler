import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Created by eschough on 2019-09-02.
 */
 
 // find command pattern from input, and convert it to cmd
 // CmdExtractor는 input으로부터 CMD pattern을 찾아 parsing
public class  CmdExtractor {
	public enum Cmds {
		CMD1("\""), CMD2("\"\""), CMD3("\"\"\""), CMD4("\"\"\"\""), CMD5("\"\"\"\"\"");	// 각 noo 명령에 대응하는 (' 가 제외된) enum
		private final String matchedStr;

		Cmds(String matchedStr) {
			this.matchedStr = matchedStr;
		}

		public String getPattern(){
			return matchedStr;
		}

		static Cmds patternType(String str) { // 쪼갠 문자열들의 패턴을 찾아 return
			for(Cmds cmds : Cmds.values()){
				if (str.equals(cmds.getPattern())){	// match 되면, 반환
					return cmds;
				}
			}
			return null;	// match 되는 것이 없을 시, null 반환
		}
    }

	private StringTokenizer stringTokenizer;

	public CmdExtractor(String nooPgm){
		stringTokenizer = new StringTokenizer(nooPgm, "'");
	}

	public CmdExtractor.Cmds patternParsing(){ // input 문자열을 쪼개서 호출 시마다 다음 token 에 맞는 Cmds 를 반환하는 메소드
		String token = stringTokenizer.nextToken();
		if(token == null)
			return null;
		return Cmds.patternType(token);
	}
}
