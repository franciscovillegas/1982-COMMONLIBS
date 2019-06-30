package portal.com.eje.frontcontroller.resobjects;

import java.util.HashMap;

public class MimeType extends HashMap<String,String> {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private static MimeType instance;
	
	private MimeType() {
		super();
		
		put("swf","application/x-shockwave-flash");
		put("xap","application/x-silverlight-app");
		put("3dm","x-world/x-3dmf");
		put("3dmf","x-world/x-3dmf");
		put("a","application/octet-stream");
		put("aab","application/x-authorware-bin");
		put("aam","application/x-authorware-map");
		put("aas","application/x-authorware-seg");
		put("abc","text/vnd.abc");
		put("acgi","text/html");
		put("afl","video/animaflex");
		put("ai","application/postscript");
		put("aif","audio/aiff");
		put("aif","audio/x-aiff");
		put("aifc","audio/aiff");
		put("aifc","audio/x-aiff");
		put("aiff","audio/aiff");
		put("aiff","audio/x-aiff");
		put("aim","application/x-aim");
		put("aip","text/x-audiosoft-intra");
		put("ani","application/x-navi-animation");
		put("aos","application/x-nokia-9000-communicator-add-on-software");
		put("aps","application/mime");
		put("arc","application/octet-stream");
		put("arj","application/arj");
		put("arj","application/octet-stream");
		put("art","image/x-jg");
		put("asf","video/x-ms-asf");
		put("asm","text/x-asm");
		put("asp","text/asp");
		put("asx","application/x-mplayer2");
		put("asx","video/x-ms-asf");
		put("asx","video/x-ms-asf-plugin");
		put("au","audio/basic");
		put("au","audio/x-au");
		put("avi","application/x-troff-msvideo");
		put("avi","video/avi");
		put("avi","video/msvideo");
		put("avi","video/x-msvideo");
		put("avs","video/avs-video");
		put("bcpio","application/x-bcpio");
		put("bin","application/mac-binary");
		put("bin","application/macbinary");
		put("bin","application/octet-stream");
		put("bin","application/x-binary");
		put("bin","application/x-macbinary");
		put("bm","image/bmp");
		put("bmp","image/bmp");
		put("bmp","image/x-windows-bmp");
		put("boo","application/book");
		put("book","application/book");
		put("boz","application/x-bzip2");
		put("bsh","application/x-bsh");
		put("bz","application/x-bzip");
		put("bz2","application/x-bzip2");
		put("c","text/plain");
		put("c","text/x-c");
		put("c++","text/plain");
		put("cat","application/vnd.ms-pki.seccat");
		put("cc","text/plain");
		put("cc","text/x-c");
		put("ccad","application/clariscad");
		put("cco","application/x-cocoa");
		put("cdf","application/cdf");
		put("cdf","application/x-cdf");
		put("cdf","application/x-netcdf");
		put("cer","application/pkix-cert");
		put("cer","application/x-x509-ca-cert");
		put("cha","application/x-chat");
		put("chat","application/x-chat");
		put("class","application/java");
		put("class","application/java-byte-code");
		put("class","application/x-java-class");
		put("com","application/octet-stream");
		put("com","text/plain");
		put("conf","text/plain");
		put("cpio","application/x-cpio");
		put("cpp","text/x-c");
		put("cpt","application/mac-compactpro");
		put("cpt","application/x-compactpro");
		put("cpt","application/x-cpt");
		put("crl","application/pkcs-crl");
		put("crl","application/pkix-crl");
		put("crt","application/pkix-cert");
		put("crt","application/x-x509-ca-cert");
		put("crt","application/x-x509-user-cert");
		put("csh","application/x-csh");
		put("csh","text/x-script.csh");
		put("css","application/x-pointplus");
		put("css","text/css");
		put("cxx","text/plain");
		put("dcr","application/x-director");
		put("deepv","application/x-deepv");
		put("def","text/plain");
		put("der","application/x-x509-ca-cert");
		put("dif","video/x-dv");
		put("dir","application/x-director");
		put("dl","video/dl");
		put("dl","video/x-dl");
		put("doc","application/msword");
		put("dot","application/msword");
		put("dp","application/commonground");
		put("drw","application/drafting");
		put("dump","application/octet-stream");
		put("dv","video/x-dv");
		put("dvi","application/x-dvi");
		put("dwf","drawing/x-dwf (old)");
		put("dwf","model/vnd.dwf");
		put("dwg","application/acad");
		put("dwg","image/vnd.dwg");
		put("dwg","image/x-dwg");
		put("dxf","application/dxf");
		put("dxf","image/vnd.dwg");
		put("dxf","image/x-dwg");
		put("dxr","application/x-director");
		put("el","text/x-script.elisp");
		put("elc","application/x-bytecode.elisp (compiled elisp)");
		put("elc","application/x-elc");
		put("env","application/x-envoy");
		put("eps","application/postscript");
		put("es","application/x-esrehber");
		put("etx","text/x-setext");
		put("evy","application/envoy");
		put("evy","application/x-envoy");
		put("exe","application/octet-stream");
		put("f","text/plain");
		put("f","text/x-fortran");
		put("f77","text/x-fortran");
		put("f90","text/plain");
		put("f90","text/x-fortran");
		put("fdf","application/vnd.fdf");
		put("fif","application/fractals");
		put("fif","image/fif");
		put("fli","video/fli");
		put("fli","video/x-fli");
		put("flo","image/florian");
		put("flx","text/vnd.fmi.flexstor");
		put("fmf","video/x-atomic3d-feature");
		put("for","text/plain");
		put("for","text/x-fortran");
		put("fpx","image/vnd.fpx");
		put("fpx","image/vnd.net-fpx");
		put("frl","application/freeloader");
		put("funk","audio/make");
		put("g","text/plain");
		put("g3","image/g3fax");
		put("gif","image/gif");
		put("gl","video/gl");
		put("gl","video/x-gl");
		put("gsd","audio/x-gsm");
		put("gsm","audio/x-gsm");
		put("gsp","application/x-gsp");
		put("gss","application/x-gss");
		put("gtar","application/x-gtar");
		put("gz","application/x-compressed");
		put("gz","application/x-gzip");
		put("gzip","application/x-gzip");
		put("gzip","multipart/x-gzip");
		put("h","text/plain");
		put("h","text/x-h");
		put("hdf","application/x-hdf");
		put("help","application/x-helpfile");
		put("hgl","application/vnd.hp-hpgl");
		put("hh","text/plain");
		put("hh","text/x-h");
		put("hlb","text/x-script");
		put("hlp","application/hlp");
		put("hlp","application/x-helpfile");
		put("hlp","application/x-winhelp");
		put("hpg","application/vnd.hp-hpgl");
		put("hpgl","application/vnd.hp-hpgl");
		put("hqx","application/binhex");
		put("hqx","application/binhex4");
		put("hqx","application/mac-binhex");
		put("hqx","application/mac-binhex40");
		put("hqx","application/x-binhex40");
		put("hqx","application/x-mac-binhex40");
		put("hta","application/hta");
		put("htc","text/x-component");
		put("htm","text/html");
		put("html","text/html");
		put("htmls","text/html");
		put("htt","text/webviewhtml");
		put("htx","text/html");
		put("ice","x-conference/x-cooltalk");
		put("ico","image/x-icon");
		put("idc","text/plain");
		put("ief","image/ief");
		put("iefs","image/ief");
		put("iges","application/iges");
		put("iges","model/iges");
		put("igs","application/iges");
		put("igs","model/iges");
		put("ima","application/x-ima");
		put("imap","application/x-httpd-imap");
		put("inf","application/inf");
		put("ins","application/x-internett-signup");
		put("ip","application/x-ip2");
		put("isu","video/x-isvideo");
		put("it","audio/it");
		put("iv","application/x-inventor");
		put("ivr","i-world/i-vrml");
		put("ivy","application/x-livescreen");
		put("jam","audio/x-jam");
		put("jav","text/plain");
		put("jav","text/x-java-source");
		put("java","text/plain");
		put("java","text/x-java-source");
		put("jcm","application/x-java-commerce");
		put("jfif","image/jpeg");
		put("jfif","image/pjpeg");
		put("jfif-tbnl","image/jpeg");
		put("jpe","image/jpeg");
		put("jpe","image/pjpeg");
		put("jpeg","image/jpeg");
		put("jpg","image/jpeg");
		put("jps","image/x-jps");
		put("js","application/x-javascript");
		put("js","application/javascript");
		put("js","application/ecmascript");
		put("js","text/javascript");
		put("js","text/ecmascript");
		put("jut","image/jutvision");
		put("kar","audio/midi");
		put("kar","music/x-karaoke");
		put("ksh","application/x-ksh");
		put("ksh","text/x-script.ksh");
		put("la","audio/nspaudio");
		put("la","audio/x-nspaudio");
		put("lam","audio/x-liveaudio");
		put("latex","application/x-latex");
		put("lha","application/lha");
		put("lha","application/octet-stream");
		put("lha","application/x-lha");
		put("lhx","application/octet-stream");
		put("list","text/plain");
		put("lma","audio/nspaudio");
		put("lma","audio/x-nspaudio");
		put("log","text/plain");
		put("lsp","application/x-lisp");
		put("lsp","text/x-script.lisp");
		put("lst","text/plain");
		put("lsx","text/x-la-asf");
		put("ltx","application/x-latex");
		put("lzh","application/octet-stream");
		put("lzh","application/x-lzh");
		put("lzx","application/lzx");
		put("lzx","application/octet-stream");
		put("lzx","application/x-lzx");
		put("m","text/plain");
		put("m","text/x-m");
		put("m1v","video/mpeg");
		put("m2a","audio/mpeg");
		put("m2v","video/mpeg");
		put("m3u","audio/x-mpequrl");
		put("man","application/x-troff-man");
		put("map","application/x-navimap");
		put("mar","text/plain");
		put("mbd","application/mbedlet");
		put("mc$","application/x-magic-cap-package-1.0");
		put("mcd","application/mcad");
		put("mcd","application/x-mathcad");
		put("mcf","image/vasa");
		put("mcf","text/mcf");
		put("mcp","application/netmc");
		put("me","application/x-troff-me");
		put("mht","message/rfc822");
		put("mhtml","message/rfc822");
		put("mid","application/x-midi");
		put("mid","audio/midi");
		put("mid","audio/x-mid");
		put("mid","audio/x-midi");
		put("mid","music/crescendo");
		put("mid","x-music/x-midi");
		put("midi","application/x-midi");
		put("midi","audio/midi");
		put("midi","audio/x-mid");
		put("midi","audio/x-midi");
		put("midi","music/crescendo");
		put("midi","x-music/x-midi");
		put("mif","application/x-frame");
		put("mif","application/x-mif");
		put("mime","message/rfc822");
		put("mime","www/mime");
		put("mjf","audio/x-vnd.audioexplosion.mjuicemediafile");
		put("mjpg","video/x-motion-jpeg");
		put("mm","application/base64");
		put("mm","application/x-meme");
		put("mme","application/base64");
		put("mod","audio/mod");
		put("mod","audio/x-mod");
		put("moov","video/quicktime");
		put("mov","video/quicktime");
		put("movie","video/x-sgi-movie");
		put("mp2","audio/mpeg");
		put("mp2","audio/x-mpeg");
		put("mp2","video/mpeg");
		put("mp2","video/x-mpeg");
		put("mp2","video/x-mpeq2a");
		put("mp3","audio/mpeg3");
		put("mp3","audio/x-mpeg-3");
		put("mp3","video/mpeg");
		put("mp3","video/x-mpeg");
		put("mpa","audio/mpeg");
		put("mpa","video/mpeg");
		put("mpc","application/x-project");
		put("mpe","video/mpeg");
		put("mpeg","video/mpeg");
		put("mpg","audio/mpeg");
		put("mpg","video/mpeg");
		put("mpga","audio/mpeg");
		put("mpp","application/vnd.ms-project");
		put("mpt","application/x-project");
		put("mpv","application/x-project");
		put("mpx","application/x-project");
		put("mrc","application/marc");
		put("ms","application/x-troff-ms");
		put("mv","video/x-sgi-movie");
		put("my","audio/make");
		put("mzz","application/x-vnd.audioexplosion.mzz");
		put("nap","image/naplps");
		put("naplps","image/naplps");
		put("nc","application/x-netcdf");
		put("ncm","application/vnd.nokia.configuration-message");
		put("nif","image/x-niff");
		put("niff","image/x-niff");
		put("nix","application/x-mix-transfer");
		put("nsc","application/x-conference");
		put("nvd","application/x-navidoc");
		put("o","application/octet-stream");
		put("oda","application/oda");
		put("omc","application/x-omc");
		put("omcd","application/x-omcdatamaker");
		put("omcr","application/x-omcregerator");
		put("p","text/x-pascal");
		put("p10","application/pkcs10");
		put("p10","application/x-pkcs10");
		put("p12","application/pkcs-12");
		put("p12","application/x-pkcs12");
		put("p7a","application/x-pkcs7-signature");
		put("p7c","application/pkcs7-mime");
		put("p7c","application/x-pkcs7-mime");
		put("p7m","application/pkcs7-mime");
		put("p7m","application/x-pkcs7-mime");
		put("p7r","application/x-pkcs7-certreqresp");
		put("p7s","application/pkcs7-signature");
		put("part","application/pro_eng");
		put("pas","text/pascal");
		put("pbm","image/x-portable-bitmap");
		put("pcl","application/vnd.hp-pcl");
		put("pcl","application/x-pcl");
		put("pct","image/x-pict");
		put("pcx","image/x-pcx");
		put("pdb","chemical/x-pdb");
		put("pdf","application/pdf");
		put("pfunk","audio/make");
		put("pfunk","audio/make.my.funk");
		put("pgm","image/x-portable-graymap");
		put("pgm","image/x-portable-greymap");
		put("pic","image/pict");
		put("pict","image/pict");
		put("pkg","application/x-newton-compatible-pkg");
		put("pko","application/vnd.ms-pki.pko");
		put("pl","text/plain");
		put("pl","text/x-script.perl");
		put("plx","application/x-pixclscript");
		put("pm","image/x-xpixmap");
		put("pm","text/x-script.perl-module");
		put("pm4","application/x-pagemaker");
		put("pm5","application/x-pagemaker");
		put("png","image/png");
		put("pnm","application/x-portable-anymap");
		put("pnm","image/x-portable-anymap");
		put("pot","application/mspowerpoint");
		put("pot","application/vnd.ms-powerpoint");
		put("pov","model/x-pov");
		put("ppa","application/vnd.ms-powerpoint");
		put("ppm","image/x-portable-pixmap");
		put("pps","application/mspowerpoint");
		put("pps","application/vnd.ms-powerpoint");
		put("ppt","application/mspowerpoint");
		put("ppt","application/powerpoint");
		put("ppt","application/vnd.ms-powerpoint");
		put("ppt","application/x-mspowerpoint");
		put("ppz","application/mspowerpoint");
		put("pre","application/x-freelance");
		put("prt","application/pro_eng");
		put("ps","application/postscript");
		put("psd","application/octet-stream");
		put("pvu","paleovu/x-pv");
		put("pwz","application/vnd.ms-powerpoint");
		put("py","text/x-script.phyton");
		put("pyc","applicaiton/x-bytecode.python");
		put("qcp","audio/vnd.qcelp");
		put("qd3","x-world/x-3dmf");
		put("qd3d","x-world/x-3dmf");
		put("qif","image/x-quicktime");
		put("qt","video/quicktime");
		put("qtc","video/x-qtc");
		put("qti","image/x-quicktime");
		put("qtif","image/x-quicktime");
		put("ra","audio/x-pn-realaudio");
		put("ra","audio/x-pn-realaudio-plugin");
		put("ra","audio/x-realaudio");
		put("ram","audio/x-pn-realaudio");
		put("ras","application/x-cmu-raster");
		put("ras","image/cmu-raster");
		put("ras","image/x-cmu-raster");
		put("rast","image/cmu-raster");
		put("rexx","text/x-script.rexx");
		put("rf","image/vnd.rn-realflash");
		put("rgb","image/x-rgb");
		put("rm","application/vnd.rn-realmedia");
		put("rm","audio/x-pn-realaudio");
		put("rmi","audio/mid");
		put("rmm","audio/x-pn-realaudio");
		put("rmp","audio/x-pn-realaudio");
		put("rmp","audio/x-pn-realaudio-plugin");
		put("rng","application/ringing-tones");
		put("rng","application/vnd.nokia.ringing-tone");
		put("rnx","application/vnd.rn-realplayer");
		put("roff","application/x-troff");
		put("rp","image/vnd.rn-realpix");
		put("rpm","audio/x-pn-realaudio-plugin");
		put("rt","text/richtext");
		put("rt","text/vnd.rn-realtext");
		put("rtf","application/rtf");
		put("rtf","application/x-rtf");
		put("rtf","text/richtext");
		put("rtx","application/rtf");
		put("rtx","text/richtext");
		put("rv","video/vnd.rn-realvideo");
		put("s","text/x-asm");
		put("s3m","audio/s3m");
		put("saveme","application/octet-stream");
		put("sbk","application/x-tbook");
		put("scm","application/x-lotusscreencam");
		put("scm","text/x-script.guile");
		put("scm","text/x-script.scheme");
		put("scm","video/x-scm");
		put("sdml","text/plain");
		put("sdp","application/sdp");
		put("sdp","application/x-sdp");
		put("sdr","application/sounder");
		put("sea","application/sea");
		put("sea","application/x-sea");
		put("set","application/set");
		put("sgm","text/sgml");
		put("sgm","text/x-sgml");
		put("sgml","text/sgml");
		put("sgml","text/x-sgml");
		put("sh","application/x-bsh");
		put("sh","application/x-sh");
		put("sh","application/x-shar");
		put("sh","text/x-script.sh");
		put("shar","application/x-bsh");
		put("shar","application/x-shar");
		put("shtml","text/html");
		put("shtml","text/x-server-parsed-html");
		put("sid","audio/x-psid");
		put("sit","application/x-sit");
		put("sit","application/x-stuffit");
		put("skd","application/x-koan");
		put("skm","application/x-koan");
		put("skp","application/x-koan");
		put("skt","application/x-koan");
		put("sl","application/x-seelogo");
		put("smi","application/smil");
		put("smil","application/smil");
		put("snd","audio/basic");
		put("snd","audio/x-adpcm");
		put("sol","application/solids");
		put("spc","application/x-pkcs7-certificates");
		put("spc","text/x-speech");
		put("spl","application/futuresplash");
		put("spr","application/x-sprite");
		put("sprite","application/x-sprite");
		put("src","application/x-wais-source");
		put("ssi","text/x-server-parsed-html");
		put("ssm","application/streamingmedia");
		put("sst","application/vnd.ms-pki.certstore");
		put("step","application/step");
		put("stl","application/sla");
		put("stl","application/vnd.ms-pki.stl");
		put("stl","application/x-navistyle");
		put("stp","application/step");
		put("sv4cpio","application/x-sv4cpio");
		put("sv4crc","application/x-sv4crc");
		put("svf","image/vnd.dwg");
		put("svf","image/x-dwg");
		put("svr","application/x-world");
		put("svr","x-world/x-svr");
		put("t","application/x-troff");
		put("talk","text/x-speech");
		put("tar","application/x-tar");
		put("tbk","application/toolbook");
		put("tbk","application/x-tbook");
		put("tcl","application/x-tcl");
		put("tcl","text/x-script.tcl");
		put("tcsh","text/x-script.tcsh");
		put("tex","application/x-tex");
		put("texi","application/x-texinfo");
		put("texinfo","application/x-texinfo");
		put("text","application/plain");
		put("text","text/plain");
		put("tgz","application/gnutar");
		put("tgz","application/x-compressed");
		put("tif","image/tiff");
		put("tif","image/x-tiff");
		put("tiff","image/tiff");
		put("tiff","image/x-tiff");
		put("tr","application/x-troff");
		put("tsi","audio/tsp-audio");
		put("tsp","application/dsptype");
		put("tsp","audio/tsplayer");
		put("tsv","text/tab-separated-values");
		put("turbot","image/florian");
		put("txt","text/plain");
		put("uil","text/x-uil");
		put("uni","text/uri-list");
		put("unis","text/uri-list");
		put("unv","application/i-deas");
		put("uri","text/uri-list");
		put("uris","text/uri-list");
		put("ustar","application/x-ustar");
		put("ustar","multipart/x-ustar");
		put("uu","application/octet-stream");
		put("uu","text/x-uuencode");
		put("uue","text/x-uuencode");
		put("vcd","application/x-cdlink");
		put("vcs","text/x-vcalendar");
		put("vda","application/vda");
		put("vdo","video/vdo");
		put("vew","application/groupwise");
		put("viv","video/vivo");
		put("viv","video/vnd.vivo");
		put("vivo","video/vivo");
		put("vivo","video/vnd.vivo");
		put("vmd","application/vocaltec-media-desc");
		put("vmf","application/vocaltec-media-file");
		put("voc","audio/voc");
		put("voc","audio/x-voc");
		put("vos","video/vosaic");
		put("vox","audio/voxware");
		put("vqe","audio/x-twinvq-plugin");
		put("vqf","audio/x-twinvq");
		put("vql","audio/x-twinvq-plugin");
		put("vrml","application/x-vrml");
		put("vrml","model/vrml");
		put("vrml","x-world/x-vrml");
		put("vrt","x-world/x-vrt");
		put("vsd","application/x-visio");
		put("vst","application/x-visio");
		put("vsw","application/x-visio");
		put("w60","application/wordperfect6.0");
		put("w61","application/wordperfect6.1");
		put("w6w","application/msword");
		put("wav","audio/wav");
		put("wav","audio/x-wav");
		put("wb1","application/x-qpro");
		put("wbmp","image/vnd.wap.wbmp");
		put("web","application/vnd.xara");
		put("wiz","application/msword");
		put("wk1","application/x-123");
		put("wmf","windows/metafile");
		put("wml","text/vnd.wap.wml");
		put("wmlc","application/vnd.wap.wmlc");
		put("wmls","text/vnd.wap.wmlscript");
		put("wmlsc","application/vnd.wap.wmlscriptc");
		put("word","application/msword");
		put("wp","application/wordperfect");
		put("wp5","application/wordperfect");
		put("wp5","application/wordperfect6.0");
		put("wp6","application/wordperfect");
		put("wpd","application/wordperfect");
		put("wpd","application/x-wpwin");
		put("wq1","application/x-lotus");
		put("wri","application/mswrite");
		put("wri","application/x-wri");
		put("wrl","application/x-world");
		put("wrl","model/vrml");
		put("wrl","x-world/x-vrml");
		put("wrz","model/vrml");
		put("wrz","x-world/x-vrml");
		put("wsc","text/scriplet");
		put("wsrc","application/x-wais-source");
		put("wtk","application/x-wintalk");
		put("xbm","image/x-xbitmap");
		put("xbm","image/x-xbm");
		put("xbm","image/xbm");
		put("xdr","video/x-amt-demorun");
		put("xgz","xgl/drawing");
		put("xif","image/vnd.xiff");
		put("xl","application/excel");
		put("xla","application/excel");
		put("xla","application/x-excel");
		put("xla","application/x-msexcel");
		put("xlb","application/excel");
		put("xlb","application/vnd.ms-excel");
		put("xlb","application/x-excel");
		put("xlc","application/excel");
		put("xlc","application/vnd.ms-excel");
		put("xlc","application/x-excel");
		put("xld","application/excel");
		put("xld","application/x-excel");
		put("xlk","application/excel");
		put("xlk","application/x-excel");
		put("xll","application/excel");
		put("xll","application/vnd.ms-excel");
		put("xll","application/x-excel");
		put("xlm","application/excel");
		put("xlm","application/vnd.ms-excel");
		put("xlm","application/x-excel");

		put("xls","application/vnd.ms-excel");
		put("xlsx","application/zip");

		put("xlt","application/excel");
		put("xlt","application/x-excel");
		put("xlv","application/excel");
		put("xlv","application/x-excel");
		put("xlw","application/excel");
		put("xlw","application/vnd.ms-excel");
		put("xlw","application/x-excel");
		put("xlw","application/x-msexcel");
		put("xm","audio/xm");
		put("xml","application/xml");
		put("xml","text/xml");
		put("xmz","xgl/movie");
		put("xpix","application/x-vnd.ls-xpix");
		put("xpm","image/x-xpixmap");
		put("xpm","image/xpm");
		put("x-png","image/png");
		put("xsr","video/x-amt-showrun");
		put("xwd","image/x-xwd");
		put("xwd","image/x-xwindowdump");
		put("xyz","chemical/x-pdb");
		put("z","application/x-compress");
		put("z","application/x-compressed");
		put("zip","application/x-compressed");
		put("zip","application/x-zip-compressed");
		put("zip","application/zip");
		put("zip","multipart/x-zip");
		put("zoo","application/octet-stream");
		put("zsh","text/x-script.zsh");

	}
	
	public String getMine(String fileName) {
		if( fileName != null && fileName.lastIndexOf(".") >= 0) {
			return get(fileName.substring(fileName.lastIndexOf(".")+1,fileName.length()).toLowerCase());
		}
		
		return "application/octet-stream";
	}
	
	public static MimeType getInstance() {
		if(instance == null) {
			synchronized(MimeType.class) {
				if(instance == null) {
					instance = new MimeType();
				}
			}
		}
		
		return instance;
	}
	
	
	/*
	.3dm  	x-world/x-3dmf
	.3dmf 	x-world/x-3dmf
	.a 	application/octet-stream
	.aab 	application/x-authorware-bin
	.aam 	application/x-authorware-map
	.aas 	application/x-authorware-seg
	.abc 	text/vnd.abc
	.acgi 	text/html
	.afl 	video/animaflex
	.ai 	application/postscript
	.aif 	audio/aiff
	.aif 	audio/x-aiff
	.aifc 	audio/aiff
	.aifc 	audio/x-aiff
	.aiff 	audio/aiff
	.aiff 	audio/x-aiff
	.aim 	application/x-aim
	.aip 	text/x-audiosoft-intra
	.ani 	application/x-navi-animation
	.aos 	application/x-nokia-9000-communicator-add-on-software
	.aps 	application/mime
	.arc 	application/octet-stream
	.arj 	application/arj
	.arj 	application/octet-stream
	.art 	image/x-jg
	.asf 	video/x-ms-asf
	.asm 	text/x-asm
	.asp 	text/asp
	.asx 	application/x-mplayer2
	.asx 	video/x-ms-asf
	.asx 	video/x-ms-asf-plugin
	.au 	audio/basic
	.au 	audio/x-au
	.avi 	application/x-troff-msvideo
	.avi 	video/avi
	.avi 	video/msvideo
	.avi 	video/x-msvideo
	.avs 	video/avs-video
	.bcpio 	application/x-bcpio
	.bin 	application/mac-binary
	.bin 	application/macbinary
	.bin 	application/octet-stream
	.bin 	application/x-binary
	.bin 	application/x-macbinary
	.bm 	image/bmp
	.bmp 	image/bmp
	.bmp 	image/x-windows-bmp
	.boo 	application/book
	.book 	application/book
	.boz 	application/x-bzip2
	.bsh 	application/x-bsh
	.bz 	application/x-bzip
	.bz2 	application/x-bzip2
	.c 	text/plain
	.c 	text/x-c
	.c++ 	text/plain
	.cat 	application/vnd.ms-pki.seccat
	.cc 	text/plain
	.cc 	text/x-c
	.ccad 	application/clariscad
	.cco 	application/x-cocoa
	.cdf 	application/cdf
	.cdf 	application/x-cdf
	.cdf 	application/x-netcdf
	.cer 	application/pkix-cert
	.cer 	application/x-x509-ca-cert
	.cha 	application/x-chat
	.chat 	application/x-chat
	.class 	application/java
	.class 	application/java-byte-code
	.class 	application/x-java-class
	.com 	application/octet-stream
	.com 	text/plain
	.conf 	text/plain
	.cpio 	application/x-cpio
	.cpp 	text/x-c
	.cpt 	application/mac-compactpro
	.cpt 	application/x-compactpro
	.cpt 	application/x-cpt
	.crl 	application/pkcs-crl
	.crl 	application/pkix-crl
	.crt 	application/pkix-cert
	.crt 	application/x-x509-ca-cert
	.crt 	application/x-x509-user-cert
	.csh 	application/x-csh
	.csh 	text/x-script.csh
	.css 	application/x-pointplus
	.css 	text/css
	.cxx 	text/plain
	.dcr 	application/x-director
	.deepv 	application/x-deepv
	.def 	text/plain
	.der 	application/x-x509-ca-cert
	.dif 	video/x-dv
	.dir 	application/x-director
	.dl 	video/dl
	.dl 	video/x-dl
	.doc 	application/msword
	.dot 	application/msword
	.dp 	application/commonground
	.drw 	application/drafting
	.dump 	application/octet-stream
	.dv 	video/x-dv
	.dvi 	application/x-dvi
	.dwf 	drawing/x-dwf (old)
	.dwf 	model/vnd.dwf
	.dwg 	application/acad
	.dwg 	image/vnd.dwg
	.dwg 	image/x-dwg
	.dxf 	application/dxf
	.dxf 	image/vnd.dwg
	.dxf 	image/x-dwg
	.dxr 	application/x-director
	.el 	text/x-script.elisp
	.elc 	application/x-bytecode.elisp (compiled elisp)
	.elc 	application/x-elc
	.env 	application/x-envoy
	.eps 	application/postscript
	.es 	application/x-esrehber
	.etx 	text/x-setext
	.evy 	application/envoy
	.evy 	application/x-envoy
	.exe 	application/octet-stream
	.f 	text/plain
	.f 	text/x-fortran
	.f77 	text/x-fortran
	.f90 	text/plain
	.f90 	text/x-fortran
	.fdf 	application/vnd.fdf
	.fif 	application/fractals
	.fif 	image/fif
	.fli 	video/fli
	.fli 	video/x-fli
	.flo 	image/florian
	.flx 	text/vnd.fmi.flexstor
	.fmf 	video/x-atomic3d-feature
	.for 	text/plain
	.for 	text/x-fortran
	.fpx 	image/vnd.fpx
	.fpx 	image/vnd.net-fpx
	.frl 	application/freeloader
	.funk 	audio/make
	.g 	text/plain
	.g3 	image/g3fax
	.gif 	image/gif
	.gl 	video/gl
	.gl 	video/x-gl
	.gsd 	audio/x-gsm
	.gsm 	audio/x-gsm
	.gsp 	application/x-gsp
	.gss 	application/x-gss
	.gtar 	application/x-gtar
	.gz 	application/x-compressed
	.gz 	application/x-gzip
	.gzip 	application/x-gzip
	.gzip 	multipart/x-gzip
	.h 	text/plain
	.h 	text/x-h
	.hdf 	application/x-hdf
	.help 	application/x-helpfile
	.hgl 	application/vnd.hp-hpgl
	.hh 	text/plain
	.hh 	text/x-h
	.hlb 	text/x-script
	.hlp 	application/hlp
	.hlp 	application/x-helpfile
	.hlp 	application/x-winhelp
	.hpg 	application/vnd.hp-hpgl
	.hpgl 	application/vnd.hp-hpgl
	.hqx 	application/binhex
	.hqx 	application/binhex4
	.hqx 	application/mac-binhex
	.hqx 	application/mac-binhex40
	.hqx 	application/x-binhex40
	.hqx 	application/x-mac-binhex40
	.hta 	application/hta
	.htc 	text/x-component
	.htm 	text/html
	.html 	text/html
	.htmls 	text/html
	.htt 	text/webviewhtml
	.htx 	text/html
	.ice 	x-conference/x-cooltalk
	.ico 	image/x-icon
	.idc 	text/plain
	.ief 	image/ief
	.iefs 	image/ief
	.iges 	application/iges
	.iges 	model/iges
	.igs 	application/iges
	.igs 	model/iges
	.ima 	application/x-ima
	.imap 	application/x-httpd-imap
	.inf 	application/inf
	.ins 	application/x-internett-signup
	.ip 	application/x-ip2
	.isu 	video/x-isvideo
	.it 	audio/it
	.iv 	application/x-inventor
	.ivr 	i-world/i-vrml
	.ivy 	application/x-livescreen
	.jam 	audio/x-jam
	.jav 	text/plain
	.jav 	text/x-java-source
	.java 	text/plain
	.java 	text/x-java-source
	.jcm 	application/x-java-commerce
	.jfif 	image/jpeg
	.jfif 	image/pjpeg
	.jfif-tbnl 	image/jpeg
	.jpe 	image/jpeg
	.jpe 	image/pjpeg
	.jpeg 	image/jpeg
	.jpeg 	image/pjpeg
	.jpg 	image/jpeg
	.jpg 	image/pjpeg
	.jps 	image/x-jps
	.js 	application/x-javascript
	.js 	application/javascript
	.js 	application/ecmascript
	.js 	text/javascript
	.js 	text/ecmascript
	.jut 	image/jutvision
	.kar 	audio/midi
	.kar 	music/x-karaoke
	.ksh 	application/x-ksh
	.ksh 	text/x-script.ksh
	.la 	audio/nspaudio
	.la 	audio/x-nspaudio
	.lam 	audio/x-liveaudio
	.latex 	application/x-latex
	.lha 	application/lha
	.lha 	application/octet-stream
	.lha 	application/x-lha
	.lhx 	application/octet-stream
	.list 	text/plain
	.lma 	audio/nspaudio
	.lma 	audio/x-nspaudio
	.log 	text/plain
	.lsp 	application/x-lisp
	.lsp 	text/x-script.lisp
	.lst 	text/plain
	.lsx 	text/x-la-asf
	.ltx 	application/x-latex
	.lzh 	application/octet-stream
	.lzh 	application/x-lzh
	.lzx 	application/lzx
	.lzx 	application/octet-stream
	.lzx 	application/x-lzx
	.m 	text/plain
	.m 	text/x-m
	.m1v 	video/mpeg
	.m2a 	audio/mpeg
	.m2v 	video/mpeg
	.m3u 	audio/x-mpequrl
	.man 	application/x-troff-man
	.map 	application/x-navimap
	.mar 	text/plain
	.mbd 	application/mbedlet
	.mc$ 	application/x-magic-cap-package-1.0
	.mcd 	application/mcad
	.mcd 	application/x-mathcad
	.mcf 	image/vasa
	.mcf 	text/mcf
	.mcp 	application/netmc
	.me 	application/x-troff-me
	.mht 	message/rfc822
	.mhtml 	message/rfc822
	.mid 	application/x-midi
	.mid 	audio/midi
	.mid 	audio/x-mid
	.mid 	audio/x-midi
	.mid 	music/crescendo
	.mid 	x-music/x-midi
	.midi 	application/x-midi
	.midi 	audio/midi
	.midi 	audio/x-mid
	.midi 	audio/x-midi
	.midi 	music/crescendo
	.midi 	x-music/x-midi
	.mif 	application/x-frame
	.mif 	application/x-mif
	.mime 	message/rfc822
	.mime 	www/mime
	.mjf 	audio/x-vnd.audioexplosion.mjuicemediafile
	.mjpg 	video/x-motion-jpeg
	.mm 	application/base64
	.mm 	application/x-meme
	.mme 	application/base64
	.mod 	audio/mod
	.mod 	audio/x-mod
	.moov 	video/quicktime
	.mov 	video/quicktime
	.movie 	video/x-sgi-movie
	.mp2 	audio/mpeg
	.mp2 	audio/x-mpeg
	.mp2 	video/mpeg
	.mp2 	video/x-mpeg
	.mp2 	video/x-mpeq2a
	.mp3 	audio/mpeg3
	.mp3 	audio/x-mpeg-3
	.mp3 	video/mpeg
	.mp3 	video/x-mpeg
	.mpa 	audio/mpeg
	.mpa 	video/mpeg
	.mpc 	application/x-project
	.mpe 	video/mpeg
	.mpeg 	video/mpeg
	.mpg 	audio/mpeg
	.mpg 	video/mpeg
	.mpga 	audio/mpeg
	.mpp 	application/vnd.ms-project
	.mpt 	application/x-project
	.mpv 	application/x-project
	.mpx 	application/x-project
	.mrc 	application/marc
	.ms 	application/x-troff-ms
	.mv 	video/x-sgi-movie
	.my 	audio/make
	.mzz 	application/x-vnd.audioexplosion.mzz
	.nap 	image/naplps
	.naplps 	image/naplps
	.nc 	application/x-netcdf
	.ncm 	application/vnd.nokia.configuration-message
	.nif 	image/x-niff
	.niff 	image/x-niff
	.nix 	application/x-mix-transfer
	.nsc 	application/x-conference
	.nvd 	application/x-navidoc
	.o 	application/octet-stream
	.oda 	application/oda
	.omc 	application/x-omc
	.omcd 	application/x-omcdatamaker
	.omcr 	application/x-omcregerator
	.p 	text/x-pascal
	.p10 	application/pkcs10
	.p10 	application/x-pkcs10
	.p12 	application/pkcs-12
	.p12 	application/x-pkcs12
	.p7a 	application/x-pkcs7-signature
	.p7c 	application/pkcs7-mime
	.p7c 	application/x-pkcs7-mime
	.p7m 	application/pkcs7-mime
	.p7m 	application/x-pkcs7-mime
	.p7r 	application/x-pkcs7-certreqresp
	.p7s 	application/pkcs7-signature
	.part 	application/pro_eng
	.pas 	text/pascal
	.pbm 	image/x-portable-bitmap
	.pcl 	application/vnd.hp-pcl
	.pcl 	application/x-pcl
	.pct 	image/x-pict
	.pcx 	image/x-pcx
	.pdb 	chemical/x-pdb
	.pdf 	application/pdf
	.pfunk 	audio/make
	.pfunk 	audio/make.my.funk
	.pgm 	image/x-portable-graymap
	.pgm 	image/x-portable-greymap
	.pic 	image/pict
	.pict 	image/pict
	.pkg 	application/x-newton-compatible-pkg
	.pko 	application/vnd.ms-pki.pko
	.pl 	text/plain
	.pl 	text/x-script.perl
	.plx 	application/x-pixclscript
	.pm 	image/x-xpixmap
	.pm 	text/x-script.perl-module
	.pm4 	application/x-pagemaker
	.pm5 	application/x-pagemaker
	.png 	image/png
	.pnm 	application/x-portable-anymap
	.pnm 	image/x-portable-anymap
	.pot 	application/mspowerpoint
	.pot 	application/vnd.ms-powerpoint
	.pov 	model/x-pov
	.ppa 	application/vnd.ms-powerpoint
	.ppm 	image/x-portable-pixmap
	.pps 	application/mspowerpoint
	.pps 	application/vnd.ms-powerpoint
	.ppt 	application/mspowerpoint
	.ppt 	application/powerpoint
	.ppt 	application/vnd.ms-powerpoint
	.ppt 	application/x-mspowerpoint
	.ppz 	application/mspowerpoint
	.pre 	application/x-freelance
	.prt 	application/pro_eng
	.ps 	application/postscript
	.psd 	application/octet-stream
	.pvu 	paleovu/x-pv
	.pwz 	application/vnd.ms-powerpoint
	.py 	text/x-script.phyton
	.pyc 	applicaiton/x-bytecode.python
	.qcp 	audio/vnd.qcelp
	.qd3 	x-world/x-3dmf
	.qd3d 	x-world/x-3dmf
	.qif 	image/x-quicktime
	.qt 	video/quicktime
	.qtc 	video/x-qtc
	.qti 	image/x-quicktime
	.qtif 	image/x-quicktime
	.ra 	audio/x-pn-realaudio
	.ra 	audio/x-pn-realaudio-plugin
	.ra 	audio/x-realaudio
	.ram 	audio/x-pn-realaudio
	.ras 	application/x-cmu-raster
	.ras 	image/cmu-raster
	.ras 	image/x-cmu-raster
	.rast 	image/cmu-raster
	.rexx 	text/x-script.rexx
	.rf 	image/vnd.rn-realflash
	.rgb 	image/x-rgb
	.rm 	application/vnd.rn-realmedia
	.rm 	audio/x-pn-realaudio
	.rmi 	audio/mid
	.rmm 	audio/x-pn-realaudio
	.rmp 	audio/x-pn-realaudio
	.rmp 	audio/x-pn-realaudio-plugin
	.rng 	application/ringing-tones
	.rng 	application/vnd.nokia.ringing-tone
	.rnx 	application/vnd.rn-realplayer
	.roff 	application/x-troff
	.rp 	image/vnd.rn-realpix
	.rpm 	audio/x-pn-realaudio-plugin
	.rt 	text/richtext
	.rt 	text/vnd.rn-realtext
	.rtf 	application/rtf
	.rtf 	application/x-rtf
	.rtf 	text/richtext
	.rtx 	application/rtf
	.rtx 	text/richtext
	.rv 	video/vnd.rn-realvideo
	.s 	text/x-asm
	.s3m 	audio/s3m
	.saveme 	application/octet-stream
	.sbk 	application/x-tbook
	.scm 	application/x-lotusscreencam
	.scm 	text/x-script.guile
	.scm 	text/x-script.scheme
	.scm 	video/x-scm
	.sdml 	text/plain
	.sdp 	application/sdp
	.sdp 	application/x-sdp
	.sdr 	application/sounder
	.sea 	application/sea
	.sea 	application/x-sea
	.set 	application/set
	.sgm 	text/sgml
	.sgm 	text/x-sgml
	.sgml 	text/sgml
	.sgml 	text/x-sgml
	.sh 	application/x-bsh
	.sh 	application/x-sh
	.sh 	application/x-shar
	.sh 	text/x-script.sh
	.shar 	application/x-bsh
	.shar 	application/x-shar
	.shtml 	text/html
	.shtml 	text/x-server-parsed-html
	.sid 	audio/x-psid
	.sit 	application/x-sit
	.sit 	application/x-stuffit
	.skd 	application/x-koan
	.skm 	application/x-koan
	.skp 	application/x-koan
	.skt 	application/x-koan
	.sl 	application/x-seelogo
	.smi 	application/smil
	.smil 	application/smil
	.snd 	audio/basic
	.snd 	audio/x-adpcm
	.sol 	application/solids
	.spc 	application/x-pkcs7-certificates
	.spc 	text/x-speech
	.spl 	application/futuresplash
	.spr 	application/x-sprite
	.sprite 	application/x-sprite
	.src 	application/x-wais-source
	.ssi 	text/x-server-parsed-html
	.ssm 	application/streamingmedia
	.sst 	application/vnd.ms-pki.certstore
	.step 	application/step
	.stl 	application/sla
	.stl 	application/vnd.ms-pki.stl
	.stl 	application/x-navistyle
	.stp 	application/step
	.sv4cpio 	application/x-sv4cpio
	.sv4crc 	application/x-sv4crc
	.svf 	image/vnd.dwg
	.svf 	image/x-dwg
	.svr 	application/x-world
	.svr 	x-world/x-svr
	.swf 	
	.t 	application/x-troff
	.talk 	text/x-speech
	.tar 	application/x-tar
	.tbk 	application/toolbook
	.tbk 	application/x-tbook
	.tcl 	application/x-tcl
	.tcl 	text/x-script.tcl
	.tcsh 	text/x-script.tcsh
	.tex 	application/x-tex
	.texi 	application/x-texinfo
	.texinfo 	application/x-texinfo
	.text 	application/plain
	.text 	text/plain
	.tgz 	application/gnutar
	.tgz 	application/x-compressed
	.tif 	image/tiff
	.tif 	image/x-tiff
	.tiff 	image/tiff
	.tiff 	image/x-tiff
	.tr 	application/x-troff
	.tsi 	audio/tsp-audio
	.tsp 	application/dsptype
	.tsp 	audio/tsplayer
	.tsv 	text/tab-separated-values
	.turbot 	image/florian
	.txt 	text/plain
	.uil 	text/x-uil
	.uni 	text/uri-list
	.unis 	text/uri-list
	.unv 	application/i-deas
	.uri 	text/uri-list
	.uris 	text/uri-list
	.ustar 	application/x-ustar
	.ustar 	multipart/x-ustar
	.uu 	application/octet-stream
	.uu 	text/x-uuencode
	.uue 	text/x-uuencode
	.vcd 	application/x-cdlink
	.vcs 	text/x-vcalendar
	.vda 	application/vda
	.vdo 	video/vdo
	.vew 	application/groupwise
	.viv 	video/vivo
	.viv 	video/vnd.vivo
	.vivo 	video/vivo
	.vivo 	video/vnd.vivo
	.vmd 	application/vocaltec-media-desc
	.vmf 	application/vocaltec-media-file
	.voc 	audio/voc
	.voc 	audio/x-voc
	.vos 	video/vosaic
	.vox 	audio/voxware
	.vqe 	audio/x-twinvq-plugin
	.vqf 	audio/x-twinvq
	.vql 	audio/x-twinvq-plugin
	.vrml 	application/x-vrml
	.vrml 	model/vrml
	.vrml 	x-world/x-vrml
	.vrt 	x-world/x-vrt
	.vsd 	application/x-visio
	.vst 	application/x-visio
	.vsw 	application/x-visio
	.w60 	application/wordperfect6.0
	.w61 	application/wordperfect6.1
	.w6w 	application/msword
	.wav 	audio/wav
	.wav 	audio/x-wav
	.wb1 	application/x-qpro
	.wbmp 	image/vnd.wap.wbmp
	.web 	application/vnd.xara
	.wiz 	application/msword
	.wk1 	application/x-123
	.wmf 	windows/metafile
	.wml 	text/vnd.wap.wml
	.wmlc 	application/vnd.wap.wmlc
	.wmls 	text/vnd.wap.wmlscript
	.wmlsc 	application/vnd.wap.wmlscriptc
	.word 	application/msword
	.wp 	application/wordperfect
	.wp5 	application/wordperfect
	.wp5 	application/wordperfect6.0
	.wp6 	application/wordperfect
	.wpd 	application/wordperfect
	.wpd 	application/x-wpwin
	.wq1 	application/x-lotus
	.wri 	application/mswrite
	.wri 	application/x-wri
	.wrl 	application/x-world
	.wrl 	model/vrml
	.wrl 	x-world/x-vrml
	.wrz 	model/vrml
	.wrz 	x-world/x-vrml
	.wsc 	text/scriplet
	.wsrc 	application/x-wais-source
	.wtk 	application/x-wintalk
	.xbm 	image/x-xbitmap
	.xbm 	image/x-xbm
	.xbm 	image/xbm
	.xdr 	video/x-amt-demorun
	.xgz 	xgl/drawing
	.xif 	image/vnd.xiff
	.xl 	application/excel
	.xla 	application/excel
	.xla 	application/x-excel
	.xla 	application/x-msexcel
	.xlb 	application/excel
	.xlb 	application/vnd.ms-excel
	.xlb 	application/x-excel
	.xlc 	application/excel
	.xlc 	application/vnd.ms-excel
	.xlc 	application/x-excel
	.xld 	application/excel
	.xld 	application/x-excel
	.xlk 	application/excel
	.xlk 	application/x-excel
	.xll 	application/excel
	.xll 	application/vnd.ms-excel
	.xll 	application/x-excel
	.xlm 	application/excel
	.xlm 	application/vnd.ms-excel
	.xlm 	application/x-excel
	.xls 	application/excel
	.xls 	application/vnd.ms-excel
	.xls 	application/x-excel
	.xls 	application/x-msexcel
	.xlt 	application/excel
	.xlt 	application/x-excel
	.xlv 	application/excel
	.xlv 	application/x-excel
	.xlw 	application/excel
	.xlw 	application/vnd.ms-excel
	.xlw 	application/x-excel
	.xlw 	application/x-msexcel
	.xm 	audio/xm
	.xml 	application/xml
	.xml 	text/xml
	.xmz 	xgl/movie
	.xpix 	application/x-vnd.ls-xpix
	.xpm 	image/x-xpixmap
	.xpm 	image/xpm
	.x-png 	image/png
	.xsr 	video/x-amt-showrun
	.xwd 	image/x-xwd
	.xwd 	image/x-xwindowdump
	.xyz 	chemical/x-pdb
	.z 	application/x-compress
	.z 	application/x-compressed
	.zip 	application/x-compressed
	.zip 	application/x-zip-compressed
	.zip 	application/zip
	.zip 	multipart/x-zip
	.zoo 	application/octet-stream
	.zsh 	text/x-script.zsh
	*/
}
