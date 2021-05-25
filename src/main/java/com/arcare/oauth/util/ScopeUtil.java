package com.arcare.oauth.util;
/**
 * 
 * @author FUHSIANG_LIU
 *
 */
public class ScopeUtil {
	/**
	 * 
	 * @param client_info.scope
	 * @param scope.id
	 * @return
	 */
	public static boolean checkScope(int clientInfoScope,int scopeId) {
		if(clientInfoScope == 1) {// all permission
			return true;
		}
		int result = clientInfoScope & scopeId;
		return result==scopeId;
	}
	
	public static void main(String args[]) {
		System.out.println(ScopeUtil.checkScope(4+16+64, 256));
	}
}
