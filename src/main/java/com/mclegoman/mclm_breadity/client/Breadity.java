/*
    Breadity
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Breadity
    Licence: GNU LGPLv3
*/

package com.mclegoman.mclm_breadity.client;

import com.mclegoman.mclm_breadity.config.BreadityConfig;

public class Breadity {
	public static void onInitializeClient() {
		System.out.println("*turns all your entities into bread*");
		BreadityConfig.init();
	}
}
