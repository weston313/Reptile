package com.wozipa.reptile.app.config;

public class Key {
		public Key(String value,String type)
		{
			this.vlaue=value;
			this.type=type;
		}
		
		private String vlaue;
		private String type;
		
		public String getVlaue() {
			return vlaue;
		}
		public void setVlaue(String vlaue) {
			this.vlaue = vlaue;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
}