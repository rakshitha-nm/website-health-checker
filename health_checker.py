import requests

def check_website(url):
    try:
        response = requests.get(url, timeout=5)

        if response.status_code == 200:
            print(f"{url} is UP (Status Code: {response.status_code})")
        else:
            print(f"{url} is DOWN (Status Code: {response.status_code})")

    except requests.exceptions.RequestException:
        print(f"{url} is DOWN (Connection Error)")


website = input("Enter website URL: ")
check_website(website)