package cl.eje.qsmcom.tool;


public class PermisosQsmCom {
		public static PermisosQsmCom ADM_CLAVES = new PermisosQsmCom("qsm_adm_claves");
		
		private String key;
		private PermisosQsmCom(String key) {
			this.key = key;
		}
		
		public String toString() {
			return key;
		}

}
