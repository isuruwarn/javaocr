package com.ocr.util;


/**
 * 
 * 
 * @author isuru
 *
 */
public enum Symbol {
	
	// character mapping
	
	AYANNA ( "\u0D85", "&#3461;", "a", 97 ),
	ANUSVARAYA ( "\u0D82", "&#3458;", "A", 65 ),
	ALPAPRAANA_BAYANNA ( "\u0DB6", "&#3510;", "b", 98 ),
	MAHAAPRAANA_BAYANNA ( "\u0DB7", "&#3511;", "B", 66 ),
	ALPAPRAANA_CHAYANNA ( "\u0DA0", "&#3488;", "c", 99 ),
	MAHAAPRAANA_CHAYANNA ( "\u0DA1", "&#3489;", "C", 67 ),
	ALPAPRAANA_DDAYANNA ( "\u0DA9", "&#3497;", "d", 100 ),
	MAHAAPRAANA_DDAYANNA ( "\u0DAA", "&#3498;", "D", 68 ),
	EYANNA ( "\u0D91", "&#3473;", "e", 101 ),
	VISARGAYA ( "\u0D83", "&#3459;", "E", 69 ),
	FAYANNA ( "\u0DC6", "&#3526;", "f", 102 ),
	KANTAJA_NAASIKYAYA ( "\u0D9E", "&#3486;", "F", 70 ),
	ALPAPRAANA_GAYANNA ( "\u0D9C", "&#3484;", "g", 103 ),
	MAHAAPRAANA_GAYANNA ( "\u0D9D", "&#3485;", "G", 71 ),
	HAYANNA ( "\u0DC4", "&#3524;", "h", 104 ),
	SANYAKA_DDAYANNA ( "\u0DAC", "&#3500;", "H", 72 ),
	IYANNA ( "\u0D89", "&#3465;", "i", 105 ),
	IIYANNA ( "\u0D8A", "&#3466;", "I", 73 ),
	ALPAPRAANA_JAYANNA ( "\u0DA2", "&#3490;", "j", 106 ),
	MAHAAPRAANA_JAYANNA ( "\u0DA3", "&#3491;", "J", 74 ),
	ALPAPRAANA_KAYANNA ( "\u0D9A", "&#3482;", "k", 107 ),
	MAHAAPRAANA_KAYANNA ( "\u0D9B", "&#3483;", "K", 75 ),
	DANTAJA_LAYANNA ( "\u0DBD", "&#3517;", "l", 108 ),
	MUURDHAJA_LAYANNA ( "\u0DC5", "&#3525;", "L", 76 ),
	MAYANNA ( "\u0DB8", "&#3512;", "m", 109 ),
	AMBA_BAYANNA ( "\u0DB9", "&#3513;", "M", 77 ),
	DANTAJA_NAYANNA ( "\u0DB1", "&#3505;", "n", 110 ),
	MUURDHAJA_NAYANNA ( "\u0DAB", "&#3499;", "N", 78 ),
	OYANNA ( "\u0D94", "&#3476;", "o", 111 ),
	IRUYANNA ( "\u0D8D", "&#3469;", "O", 79 ),
	ALPAPRAANA_PAYANNA ( "\u0DB4", "&#3508;", "p", 112 ),
	MAHAAPRAANA_PAYANNA ( "\u0DB5", "&#3509;", "P", 80 ),
	TAALUJA_NAASIKYAYA ( "\u0DA4", "&#3492;", "q", 113 ),
	TAALUJA_SANYOOGA_NAAKSIKYAYA ( "\u0DA5", "&#3493;", "Q", 81 ),
	RAYANNA ( "\u0DBB", "&#3515;", "r", 114 ),
	RAKAYANSAYA ( "\u0DCA\u200D\u0DBB", "&#3530;&#8205;&#3515;", "R", 82 ),
	DANTAJA_SAYANNA ( "\u0DC3", "&#3523;", "s", 115 ),
	SANYAKA_DAYANNA ( "\u0DB3", "&#3507;", "S", 83 ),
	ALPAPRAANA_TTAYANNA ( "\u0DA7", "&#3495;", "t", 116 ),
	MAHAAPRAANA_TTAYANNA ( "\u0DA8", "&#3496;", "T", 84 ),
	UYANNA ( "\u0D8B", "&#3467;", "u", 117 ),
	IRUUYANNA ( "\u0D8E", "&#3470;", "U", 85 ),
	VAYANNA ( "\u0DC0", "&#3520;", "v", 118 ),
	SANYAKA_GAYANNA ( "\u0D9F", "&#3487;", "V", 86 ),
	ALPAPRAANA_DAYANNA ( "\u0DAF", "&#3503", "w", 119 ),
	MAHAAPRAANA_DAYANNA ( "\u0DB0", "&#3504", "W", 87 ),
	TAALUJA_SHAYANNA ( "\u0DC1", "&#3521", "x", 120 ),
	MUURDHAJA_SHAYANNA ( "\u0DC2", "&#3522", "X", 88 ),
	YAYANNA ( "\u0DBA", "&#3514", "y", 121 ),
	YANSAYA ( "\u0DCA\u200D\u0DBA", "&#3530;&#8205;&#3514", "Y", 89 ),
	ALPAPRAANA_TAYANNA ( "\u0DAD", "&#3501", "z", 122 ),
	MAHAAPRAANA_TAYANNA ( "\u0DAE", "&#3502", "Z", 90 ),
	AELA_PILLA ( "\u0DCF", "&#3535", "\\", 92 ),
	AL_LAKUNA ( "\u0DCA", "&#3530", "|", 124 ),
	KETTI_IS_PILLA ( "\u0DD2", "&#3538", "`", 96 ),
	DIGA_IS_PILLA ( "\u0DD3", "&#3539", "~", 126 ),
	KOMBUVA ( "\u0DD9", "&#3545", "@", 64 ),
	DIGA_KOMBUVA ( "\u0DDA", "&#3546", "^", 94 ),
	GAETTA_PILLA ( "\u0DD8", "&#3544", "_", 95 ),
	PAA_PILLA ( "\u0DD4", "&#3540", "[", 91 ),
	DIGA_PAA_PILLA ( "\u0DD6", "&#3542", "{", 123 ),
	AEDA_PILLA ( "\u0DD0", "&#3536", "]", 93 ),
	DIGA_AEDA_PILLA ( "\u0DD1", "&#3537", "}", 125 ),
	KOMBUVA_HAA_AELA_PILLA ( "\u0DDC", "&#3548", "<", 60 ),
	KOMBUVA_HAA_DIGA_AELA_PILLA ( "\u0DDD", "&#3549", ">", 62 ),
	KSHA ( "\u0D9A\u0DCA\u200D\u0DC2", "&#3482;&#3530;&#8205;&#3522;", "1304", 1304 ),
	THALUJA_NASIKAYA ( "\u0DA4", "&#3492;", "1305", 1305 ),
	SANYOGA_NASIKAYA ( "\u0DA5", "&#3493;", "1306", 1306 ),
	KOMBU_DEKA ( "\u0DDB", "&#3547;", "1320", 1320 ),
	KOMBUVA_HAA_GAYANUKITTA ( "\u0DDE", "&#3550;", "1321", 1321 ),
	DIGA_GAETTA_PILLA ( "\u0DF2", "&#3570;", "1322", 1322 ),
	GAYANUKITTA ( "\u0DDF", "&#3551;", "1323", 1323 ),
	DIGA_GAYANUKITTA ( "\u0DF3", "&#3571;", "1324", 1324 ),
	AAYANNA ( "\u0D86", "&#3462;", "1400", 1400 ),
	AEYANNA ( "\u0D87", "&#3463;", "1401", 1401 ),
	AEEYANNA ( "\u0D88", "&#3464;", "1402", 1402 ),
	EEYANNA ( "\u0D92", "&#3474;", "1403", 1403 ),
	AIYANNA ( "\u0D93", "&#3475;", "1404", 1404 ),
	OOYANNA ( "\u0D95", "&#3477;", "1405", 1405 ),
	AUYANNA ( "\u0D96", "&#3478;", "1406", 1406 ),
	UUYANNA ( "\u0D8C", "&#3468;", "1407", 1407 ),
	SANYAKA_JAYANNA ( "\u0DA6", "&#3494;", "1408", 1408 );
	
	
	private final String UNICODE; // Unicode value of the symbol
	private final String HTMLCODE; // HTML value of the symbol
	private final String KEYCODE; // the keyboard character (input key) this particular symbol is mapped to
	private final int ASCIICODE; // the ASCII value of the keyboard character (input key)
	
	
	Symbol( String unicode, String htmlCode, String keyCode, int asciiCode ) {
		this.UNICODE = unicode;
		this.HTMLCODE = htmlCode;
		this.KEYCODE = keyCode;
		this.ASCIICODE = asciiCode;
	}
	
	
	public String getUnicodeValue() {
		return UNICODE;
	}
	
	public String getHtmlCode() {
		return HTMLCODE;
	}


	public String getKeyCode() {
		return KEYCODE;
	}
	
	public int getAsciiCode() {
		return ASCIICODE;
	}
	
	
	public static String getSymbolForASCII( int asciiCode ) {
		for(Symbol symbol: Symbol.values() ) {
			if( symbol.getAsciiCode() == asciiCode ) {
				return symbol.getUnicodeValue();
			}
		}
		return null;
	}
	
	public static String getSymbolNameForASII( int asciiCode ) {
		for(Symbol symbol: Symbol.values() ) {
			if( symbol.getAsciiCode() == asciiCode ) {
				return symbol.name();
			}
		}
		return null;
	}

}
