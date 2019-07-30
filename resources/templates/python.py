import requests

response = requests.{{request-method|name}}(
    '{{scheme}}://{{server-name}}{{uri}}',
    {% if query-string %}params='{{query-string|safe}}',{% endif %}
    {% if headers %}headers={{headers|json|safe}},{% endif %}
    {% if body %}json=({{body|json|safe}}){% endif %}
)

{% ifequal request-method :delete %}
print({'raw_body': response.text, 'status': response.status_code, 'code': response.status_code})
{% else %}
print({'raw_body': response.json(), 'status': response.status_code, 'code': response.status_code})
{% endifequal %}
