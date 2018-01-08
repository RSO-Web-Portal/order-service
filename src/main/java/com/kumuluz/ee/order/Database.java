/*
 *  Copyright (c) 2014-2017 Kumuluz and/or its affiliates
 *  and other contributors as indicated by the @author tags and
 *  the contributor list.
 *
 *  Licensed under the MIT License (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  https://opensource.org/licenses/MIT
 *
 *  The software is provided "AS IS", WITHOUT WARRANTY OF ANY KIND, express or
 *  implied, including but not limited to the warranties of merchantability,
 *  fitness for a particular purpose and noninfringement. in no event shall the
 *  authors or copyright holders be liable for any claim, damages or other
 *  liability, whether in an action of contract, tort or otherwise, arising from,
 *  out of or in connection with the software or the use or other dealings in the
 *  software. See the License for the specific language governing permissions and
 *  limitations under the License.
*/
package com.kumuluz.ee.order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Database {
    private static List<Order> orders = new ArrayList<>(
            Arrays.asList(
                    new Order("1", "Paket 1", new String[] {"1"}, new int[] {1}, "1", "1"),
                    new Order("2", "Paket 2", new String[] {"1"}, new int[] {3}, "1", "2"),
                    new Order("3", "Paket 3", new String[] {"2"}, new int[] {2}, "2", "4"),
                    new Order("4", "Paket 4", new String[] {"2", "3"}, new int[] {1,1}, "3", "4"),
                    new Order("5", "Paket 5", new String[] {"4", "5"}, new int[] {1,1}, "4", "3")
            )
    );



    public static List<Order> getOrders() {
        return orders;
    }

    public static Order getOrder(String id) {

        for (Order order : orders) {
            if (order.getId().equals(id))
                return order;
        }

        return null;
    }


    public static void addOrder(Order order) {
        orders.add(order);
    }

    public static void deleteOrder(String id) {
        for (Order order : orders) {
            if (order.getId().equals(id)) {
                orders.remove(order);
                break;
            }
        }
    }
}
