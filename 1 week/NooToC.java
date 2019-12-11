import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

/*
 * Created by eschough on 2019-09-02.
 */
public class NooToC {
    CmdExtractor cmdExtractor;
    FileWriter fw;
    String nooPgm, cPgm;
    int tab = 1;

    // init
    public NooToC(FileWriter fw, String nooPgm) {
        this.fw = fw;
        this.nooPgm = nooPgm;
        cmdExtractor = new CmdExtractor(nooPgm);
    }

    // translate cmd to C code for each case.
    public void translate(CmdExtractor.Cmds cmd) throws IOException {
        cPgm = "#include <stdio.h>\nint main() {\n\tint r, t1, t2, t3;\n\n";    // 처음 공통 부분 삽입
        patternTranslate(cmd);  // noo code 를 c code 로 translate
        cPgm += "\n\treturn 1;\n}\n";   // 마지막 공통 부분 삽입
        saveFile(); // translate 한 파일 저장
    }

    private void patternTranslate(CmdExtractor.Cmds cmd){
        switch (cmd){
            case CMD1:  // x의 결과를 출력하고 리턴 (print)
                patternTranslate(this.next());  // (x)
                addTabStr("printf(\"%d\", r);\n");
                break;

            case CMD2:  // x + 1 결과를 리턴 (int)
                patternTranslate(this.next());  // (x)
                addTabStr("t1 = r;\n");
                addTabStr("r = t1 + 1;\n");
                break;

            case CMD3:  // 0을 리턴 (0을 리턴)
                addTabStr("r = 0;\n");
                break;

            case CMD4:  // x를 수행 후 y를 수행하고 y 결과를 리턴 (순차 실행)
                patternTranslate(this.next());  // (x)
                patternTranslate(this.next());  // (y)
                break;

            case CMD5:  // 수행된 y나 z의 결과값이 리턴값이 됨 (if 문)
                patternTranslate(this.next()); // 조건문 부분 (x)
                addTabStr("t1 = r;\n");
                addTabStr("if (t1 != 0)\n");

                addTabStr("{\n");
                tab++;  // if 문 들여쓰기
                patternTranslate(this.next()); // if 문 부분 (y)
                tab--;  // 들여쓰기 복원

                addTabStr("}\n");
                addTabStr("else\n");
                addTabStr("{\n");
                tab++;  // else 문 들여쓰기
                patternTranslate(this.next()); // else 문 부분 (z)
                tab--;  // 들여쓰기 복원
                addTabStr("}\n");
                break;

            default:
                break;
        }
    }

    private void addTabStr(String str){ // 코드의 들여쓰기에 맞게 tab을 삽입하는 메소드
        for (int i = 0; i < tab; i++)
            cPgm += "\t";
        cPgm += str;
    }

    private void saveFile() throws IOException {    // .c 파일로 저장하는 메소드
        fw.write(cPgm);
    }

    public CmdExtractor.Cmds next() {   // 다음 noo 명령을 가져오는 메소드
        return cmdExtractor.patternParsing();
    }
}
