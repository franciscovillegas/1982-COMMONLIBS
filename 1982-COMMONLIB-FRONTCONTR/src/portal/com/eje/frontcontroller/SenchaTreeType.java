package portal.com.eje.frontcontroller;

import portal.com.eje.portal.organica.vo.UnidadGenericaStyleDef;

public enum SenchaTreeType {
 
		check(new UnidadGenericaStyleDef().setIconAgrupa("../../images/btns/checkbox_null.png").setIconHoja("../../images/btns/checkbox_null.png")
										  .setIconHojaSelected("../../images/btns/checkbox_add.png").setIconAgrupaSelected("../../images/btns/checkbox_add.png")),
		normal(new UnidadGenericaStyleDef()),
		personalizado(new UnidadGenericaStyleDef());
		
		private UnidadGenericaStyleDef u;
		SenchaTreeType(UnidadGenericaStyleDef u) {
			this.u = u;
		}
		
		public UnidadGenericaStyleDef getStyle() {
			return this.u;
		}
	 
	
}
