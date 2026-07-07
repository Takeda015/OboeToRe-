package com.jp3.config;
//Spring Security設定（認証・認可・除外パス定義）

//ログイン

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // このクラスがBean定義の設定クラスであることを宣言
@EnableWebSecurity // Spring Securityを有効化
public class SecurityConfig {
	@Bean // SecurityFilterChainをSpringコンテナに登録
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
				// URLごとのアクセス制御
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/login", "/register", "/register/check-user_id", "/css/**",
								"/script/**",
								"/img/**")
						.permitAll() // /loginは未認証でもアクセス可
						.anyRequest().authenticated() // それ以外は認証必須
				)

				// フォームログインの設定
				.formLogin(form -> form
						.usernameParameter("user_id") // ここでユーザの入力する判定name属性を変更
						.loginPage("/login") // ログインページのURL（GETで表示）
						.defaultSuccessUrl("/home") // 認証成功時のリダイレクト先
						.failureUrl("/login?error") // 認証失敗時のリダイレクト先

				)

				//ログアウトの設定
				.logout(logout -> logout
						.logoutUrl("/logout") // ログアウトURL
						.logoutSuccessUrl("/login?logout") // ログアウト後の遷移先
						.invalidateHttpSession(true) // セッション破棄
						.deleteCookies("JSESSIONID") // Cookie削除
						.permitAll());

		return http.build(); // 設定を確定してFilterChainを生成
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		// Argon2でハッシュ化する
		return new Argon2PasswordEncoder(
				16, // saltLength: ソルトのバイト長
				32, // hashLength: ハッシュのバイト長
				1, // parallelism: 並列度
				777, // memory: 使用メモリ(KB)
				3 // iterations: ハッシュ反復回数
		);
	}
}
