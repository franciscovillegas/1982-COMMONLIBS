package cl.eje.qsmcom.tipo;


public enum TipoCarga {

		calculo ( 2  ),
		produccion ( 1 );
		private int id;
		private String key;
		
		TipoCarga(int id ) {
			this.id = id;
		}
		
		public int getId() {
			return this.id;
		}

}
