#include<iostream>
#include<vector>
#include<algorithm>
using namespace std;
void cinVector(vector<int>& numbers, int size)
{
	int number;
	for (int i = 0; i < size; i++)
	{
		cin >> number;
		numbers.push_back(number);
	}
}
void coutVector(vector<int> numbers)
{
    if (numbers.size() != 0)
    {
        for (size_t i = 0; i < numbers.size(); i++)
        {
            cout << numbers[i] << " ";
        }
    }
}
int solution(vector<int> numbers)
{
	sort(numbers.begin(), numbers.end());
	if (numbers[numbers.size() - 1] <= 0)
	{
		return 1;
	}
	int indexFirstPositive = 0;
	int result = 1;
	for (int i = 1; i < numbers.size(); i++)
	{
		if (numbers[i - 1] <= 0 && numbers[i] > 0)
		{
			indexFirstPositive = i;
			break;
		}
	}
	
	if (numbers[indexFirstPositive] != 1)
	{
		return 1;
	}
	else
	{
		for (int k = indexFirstPositive; k < numbers.size()-1; k++)
		{
			if (numbers[k + 1] - numbers[k] > 1)
			{
				return numbers[k] + 1;
			}
		}
		return numbers[numbers.size() - 1] + 1;
	}
}
int main()
{
	
	vector<int> kitNumbers;
	int size;
	cin >> size;
	cinVector(kitNumbers, size);
	cout << solution(kitNumbers);
	return 0;
}