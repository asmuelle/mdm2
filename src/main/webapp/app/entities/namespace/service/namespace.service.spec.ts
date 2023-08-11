import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { INamespace } from '../namespace.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../namespace.test-samples';

import { NamespaceService } from './namespace.service';

const requireRestSample: INamespace = {
  ...sampleWithRequiredData,
};

describe('Namespace Service', () => {
  let service: NamespaceService;
  let httpMock: HttpTestingController;
  let expectedResult: INamespace | INamespace[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(NamespaceService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Namespace', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const namespace = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(namespace).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Namespace', () => {
      const namespace = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(namespace).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Namespace', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Namespace', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Namespace', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addNamespaceToCollectionIfMissing', () => {
      it('should add a Namespace to an empty array', () => {
        const namespace: INamespace = sampleWithRequiredData;
        expectedResult = service.addNamespaceToCollectionIfMissing([], namespace);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(namespace);
      });

      it('should not add a Namespace to an array that contains it', () => {
        const namespace: INamespace = sampleWithRequiredData;
        const namespaceCollection: INamespace[] = [
          {
            ...namespace,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addNamespaceToCollectionIfMissing(namespaceCollection, namespace);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Namespace to an array that doesn't contain it", () => {
        const namespace: INamespace = sampleWithRequiredData;
        const namespaceCollection: INamespace[] = [sampleWithPartialData];
        expectedResult = service.addNamespaceToCollectionIfMissing(namespaceCollection, namespace);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(namespace);
      });

      it('should add only unique Namespace to an array', () => {
        const namespaceArray: INamespace[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const namespaceCollection: INamespace[] = [sampleWithRequiredData];
        expectedResult = service.addNamespaceToCollectionIfMissing(namespaceCollection, ...namespaceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const namespace: INamespace = sampleWithRequiredData;
        const namespace2: INamespace = sampleWithPartialData;
        expectedResult = service.addNamespaceToCollectionIfMissing([], namespace, namespace2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(namespace);
        expect(expectedResult).toContain(namespace2);
      });

      it('should accept null and undefined values', () => {
        const namespace: INamespace = sampleWithRequiredData;
        expectedResult = service.addNamespaceToCollectionIfMissing([], null, namespace, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(namespace);
      });

      it('should return initial array if no Namespace is added', () => {
        const namespaceCollection: INamespace[] = [sampleWithRequiredData];
        expectedResult = service.addNamespaceToCollectionIfMissing(namespaceCollection, undefined, null);
        expect(expectedResult).toEqual(namespaceCollection);
      });
    });

    describe('compareNamespace', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareNamespace(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareNamespace(entity1, entity2);
        const compareResult2 = service.compareNamespace(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareNamespace(entity1, entity2);
        const compareResult2 = service.compareNamespace(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareNamespace(entity1, entity2);
        const compareResult2 = service.compareNamespace(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
