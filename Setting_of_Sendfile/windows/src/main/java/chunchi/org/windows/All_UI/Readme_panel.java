package chunchi.org.windows.All_UI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Panel;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;

public class Readme_panel extends Panel{

	private static final long serialVersionUID = 1L;
	
	public JTextArea readme_info1;
	public JScrollPane js_of_readme_info1;
	public All_UI aui;

	public Readme_panel(All_UI aui) {	
		UIManager.put("TextArea.font", new Font("Courier", Font.BOLD, 24));
		
		this.aui = aui;	
		readme_info1 = new JTextArea();
		readme_info1.append("以下介紹一些參數的意義與如何輸入\n");
		readme_info1.append("1.最大執行時間:\n");
		readme_info1.append("  如果複製檔案花費的時間超過設定的最大時間，將會暫停並等待使用者回覆是否繼續執行\n");
		readme_info1.append("  預設值為 2^31-1 hours\n");
		readme_info1.append("2.execute timing:\n");
		readme_info1.append("  now: 輸入完成按下submit後立即執行 \n");
		readme_info1.append("  periodical: 輸入完成按下submit後會根據條件週期性執行 \n");
		readme_info1.append("3.source_mount_folder:\n");
		readme_info1.append("  (1).如果是建立暫時連線的話，mount到遠端的目標資料夾，會拿來當作根目錄\n");
		readme_info1.append("  (2).如果已經連線的話，那麼這個選項就是本機/已連線遠端的資料夾\n");
		readme_info1.append("4.folder_to_be_copied:\n");
		readme_info1.append("  只有建立暫時連線，才需要填的選項，用來填入想要複製的資料夾\n");
		readme_info1.append("  Example: 假設連線到 CC$當作根目錄並且要複製底下的abc\\def資料夾， \n");
		readme_info1.append("           那麼source_folder填入CC$並且 folder_to_be_copied填入 abc\\def \n");
		readme_info1.append("5.mount_disk:\n");
		readme_info1.append("  建立遠端連線時本機的接口，只能輸入英文字母一個+:。 \n");
		readme_info1.append("  Example: Z:,W: 並且注意該磁碟機不能被占用，例如 C:是系統磁碟機 \n");
		readme_info1.append("6.force_umount:\n");
		readme_info1.append("  建立暫時連線時如果發現該磁碟機已被占用，是否強行中止前工作以將磁碟機\n\n");
		
		readme_info1.append("以下舉例一些複製檔案的輸入法\n");
		readme_info1.append("1. 假設你想要複製遠端(未連線)的 CC$資料夾中的 CC$\\asd\\zxc \n"
				+ "  與 CC$\\asd\\qwe 那麼 folder_to_be_copied 應該輸入 asd\\zxc;asd\\qwe\n");
		readme_info1.append("2. 假設你想要複製遠端(未連線)的 CC$資料夾的所有內容 \n"
				+ "  那麼 folder_to_be_copied應該填入 *\n");
		readme_info1.append("3. 假設你想要複製遠端(已連線)的 CC$資料夾中的 CC$\\asd\\zxc，並且mount CC$資料夾到 w:上，\n"
				+ "  那麼 source_mount_folder應該填入  w:\\asd\\zxc \n\n");
		
		readme_info1.append("以下有三個情境與右邊有對應的三個圖片:\n");
		readme_info1.append("   Example1 : 將10.30.9.32\\wang\\abc\\def  與 \n" + 
						   " 10.30.9.32\\wang\\abc\\ghi 資料夾複製到 172.23.59.15\\CC$。\n" + 
						   " 其中 10.30.9.32\\wang 與本機的 s: 磁碟機建立暫時nfs連線 ; \n" + 
						   " 172.23.59.15\\CC$ 與本機的 t: 磁碟機建立暫時cifs連線 。\n");
		readme_info1.append("   Example2 : 將172.23.59.15\\CC$\\abc\\def與 \n" + 
							" 172.23.59.15\\CC$\\abc\\ghi 資料夾複製到 10.30.9.32\\wang\\zxcv。\n" + 
							" 其中 172.23.59.15\\CC$ 與本機的 s: 磁碟機建立暫時nfs連線 ; \n" + 
							" 並且 10.30.9.32\\wang 已經與 t: 建立連線\n");
		readme_info1.append("   Example3 : 將172.23.59.15\\CC$\\zxcv 複製到本機\n" + 
							" 的 C:\\Users\\William\\Desktop\\abc 資料夾。其中 172.23.59.15\\CC$\n" + 
							" 與本機的 s: 磁碟機已經建立連線。\n");	
		readme_info1.setEditable(false);
		js_of_readme_info1 = new JScrollPane(readme_info1,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		readme_info1.setPreferredSize(new Dimension(1200,1600));
		js_of_readme_info1.setPreferredSize(new Dimension(1200,800));
		js_of_readme_info1.setVisible(true);
		this.add(js_of_readme_info1);	
		this.setBounds(aui.getBounds());
		this.setVisible(true);		
	}
		
}
