import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 平滑加权轮询算法
 * @author km_yang
 * @create 2021/8/6 14:41
 *
 * 该算法的原理如下：
 *
 * 　　每个服务器都有两个权重变量：
 *
 * 　　a：weight，配置文件中指定的该服务器的权重，这个值是固定不变的；
 *
 * 　　b：current_weight，服务器目前的权重。一开始为0，之后会动态调整。
 *
 * 　　每次当请求到来，选取服务器时，会遍历数组中所有服务器。对于每个服务器，让它的current_weight增加它的weight；
 * 同时累加所有服务器的weight，并保存为total。
 *
 * 　　遍历完所有服务器之后，如果该服务器的current_weight是最大的，就选择这个服务器处理本次请求。
 * 最后把该服务器的current_weight减去total。
 *
 * http {
 *     upstream cluster {
 *         server a weight=4;
 *         server b weight=2;
 *         server c weight=1;
 *     }
 *     ...
 * }
 *
 *  请求序号 current_weight(before)  select  current_weight(after)
 *  1       {4,2,1}                 a       {-3,2,1}
 *  2       {1,4,2}                 b       {1,-3,2}
 *  3       {5,-1,3}                a       {-2,-1,3}
 *  4       {2,1,4}                 c       {2,1,-3}
 *  5       {6,3,-2}                a       {-1,3,-2}
 *  6       {3,5,-1}                b       {3,-2,-1}
 *  7       {7,0,0}                 a       {0,0,0}
 *
 */
public class SmoothWeightedRoundRobin {

    /**
     * 服务器列表
     */
    private List<Server> serverList = new ArrayList<>();

    public SmoothWeightedRoundRobin() {
        serverList.add(new Server("127.0.0.1", 4, "serverA"));
        serverList.add(new Server("127.0.0.2", 2, "serverB"));
        serverList.add(new Server("127.0.0.3", 1, "serverC"));
    }

    public Server getServer() {
        // 对于每个服务器，让它的current_weight增加它的weight；
        serverList.stream().forEach(server -> server.setCurrentWeight(server.getCurrentWeight() + server.getWeight()));
        // 同时累加所有服务器的weight，并保存为total。
        int totalAmount = serverList.stream().mapToInt(Server::getCurrentWeight).sum();
        // 遍历完所有服务器之后，如果该服务器的current_weight是最大的，就选择这个服务器处理本次请求。
        Server server = serverList.stream().max(Comparator.comparing(Server::getCurrentWeight)).get();
        // 最后把该服务器的current_weight减去total。
        server.setCurrentWeight(server.getCurrentWeight() - totalAmount);
        return server;
    }

    public static void main(String[] args) {
        SmoothWeightedRoundRobin smoothWeightedRoundRobin = new SmoothWeightedRoundRobin();
        for (int i = 0; i < 7; i++) {
            Server server = smoothWeightedRoundRobin.getServer();
            System.out.println(String.format("alias:%s\t weight:%s\t",
                server.getAlias(), server.getWeight()));
        }
    }

    class Server {
        private String ip;
        private int weight;
        private int currentWeight;
        private String alias;

        public Server(String ip, int weight, String alias) {
            this.ip = ip;
            this.weight = weight;
            this.alias = alias;
        }

        public String getAlias() {
            return alias;
        }

        public String getIp() {
            return ip;
        }

        public int getWeight() {
            return weight;
        }

        public int getCurrentWeight() {
            return currentWeight;
        }

        public void setCurrentWeight(int currentWeight) {
            this.currentWeight = currentWeight;
        }
    }

}
