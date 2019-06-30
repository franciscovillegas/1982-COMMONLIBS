package portal.com.eje.tools.jqtree;

import java.sql.SQLException;

import portal.com.eje.frontcontroller.IOClaseWeb;

public abstract class AJqTreeMaker {
		protected IOClaseWeb ioClaseWeb;
	
		public AJqTreeMaker(IOClaseWeb ioClaseWeb) {
			this.ioClaseWeb = ioClaseWeb;
		}
	
		public abstract JqNodos getTree() throws SQLException;
}
