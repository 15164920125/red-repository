package red.com.commons;

import org.springframework.stereotype.Component;

import lombok.Data;
/**
 * 配置所有的服务名
 * @author ASUS
 *
 */
@Data
@Component
public class Server {
	private String echo;
	// 理算微服务（理算）
	private String compensate;
	// 查勘微服务
	private String check;
	// 立案微服务--->立案模块
	private String claim;
	// 立案微服务--->结案模块
	private String claimServerEndCase;
	// 辅助微服务--->辅助模块
	private String auxiliaryServer;
	// 查勘微服务--->查勘环节（查勘，理赔日志，赔案名称，客户案号，工作进展备忘）模块
	private String checkbizServer;
	// 理算微服务--->预赔模块
	private String compensatebizServer;
	// 定损微服务--->损余回收模块
	private String deflossbizServer;
	// 定损微服务--->旧人伤模块
	private String deflossbizServerBodily;
	// 周边交互微服务
	private String peripheralServer;
	// 理算微服务（核赔）
	private String undwrt;
	// 立案微服务==>第三方机构
	private String claimServerAgency;
	// 立案微服务==>担保信息
	private String claimServerAssure;
	// 立案微服务---诉讼模块
	// private String claimbizServerLawsuit;
	// 理算微服务
	private String compensatebizServerSalvage;
	// 理算微服务--->领款人
	private String compensatePayPerson;
	// 理算微服务--->保单信息模块
	private String peripheralServerPolicyInfo;
	// 周边交互微服务(客户等级模型)
	private String peripheralbiz;
	// 周边交互微服务(保单信息模块)
	private String peripheralbizServer;
	// 工作流微服务--->工作流模块
	private String bpmServer;
	// 理算微服务（预赔）
	private String prepay;
	// 理算微服务（医疗审核）
	private String medicalAudit;
	// 理算微服务（追偿）
	private String recovery;
	// 周边交互微服务（规则/报告）
	private String ilog;
	// 立案微服务-->关键环节管控
	private String claimServercontrol;
	// 立案微服务-->诉讼
	private String claimbizServer;
	//立案微服务-->资料收集
	private String dataCollectServer;
	
	//理算微服务-->残值处理
	private String compensateSalvage;
	//立案微服务-->索赔人信息
	private String claimServerClaimant;
}
