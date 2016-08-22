#!/usr/bin/python

import json
import urllib

def bump_revision(version_str):
    ver_parts = version_str.split('.')
    if len(ver_parts) != 3:
        return 'error: not valid version'
    new_rev = int(ver_parts[2])+1
    return "{0}.{1}.{2}".format(ver_parts[0],ver_parts[1],new_rev)

url="https://docker.us-east-1.aws.aol.com/artifactory/api/storage/docker-us-east-1-local/metro/tsdb-splicer"
response = urllib.urlopen(url)
response_json = json.loads(response.read())

versions=[]
if response_json.has_key('children'):
    for item in response_json['children']:
        # strip path from front
        ver=item['uri'][1:]
        if ver[:1].isdigit():
            versions.append(ver)
    #print versions
    sversions=sorted(versions)
    print bump_revision(sversions[-1])