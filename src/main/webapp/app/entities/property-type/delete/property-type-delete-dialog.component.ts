import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IPropertyType } from '../property-type.model';
import { PropertyTypeService } from '../service/property-type.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './property-type-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PropertyTypeDeleteDialogComponent {
  propertyType?: IPropertyType;

  constructor(protected propertyTypeService: PropertyTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.propertyTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
