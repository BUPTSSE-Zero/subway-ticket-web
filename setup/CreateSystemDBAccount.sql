/**
 * Author:  buptsse-zero <GGGZ-1101-28@Live.cn>
 * Created: May 4, 2016
 */

/*Please run this script with root privilege.*/
CREATE USER 'SubwayTicketWeb'@'127.0.0.1' IDENTIFIED BY '4af8c2909cee0431cae76d45ef740152d144ed51de1af8cde9f836b2158a8bae';
GRANT ALL ON SubwayTicketDB.* TO 'SubwayTicketWeb'@'127.0.0.1';