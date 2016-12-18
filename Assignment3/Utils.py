import urllib2
import urllib3
import urllib
import json

http = urllib3.PoolManager()
urllib3.disable_warnings()


def send_get_request(url):
    # req = urllib2.Request(url)
    # res_data = urllib2.urlopen(req)
    # res = res_data.read()
    # http_response = json.loads(res)
    res = http.request('GET', url)
    print res.data
    http_response = json.loads(res.data)
    return http_response


def send_post_request(dicts, remote_url):
    body = json.dumps(dicts)
    http_response = dict()
    #url_encoded_data = urllib.urlencode(body)
    # try:
    #     req = urllib2.Request(url=remote_url, data=body)
    #     res_data = urllib2.urlopen(req)
    #     res = res_data.read()
    #     if res is None:
    #         return None
    #     http_response = json.loads(res)
    # except urllib2.HTTPError as e:
    #     print e.reason

    res = http.request(
        'POST',
        remote_url,
        body=body
    )
    res_data = res.data
    http_response = json.loads(res_data)

    return http_response
